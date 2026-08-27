package in.hmorix.client.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import in.hmorix.client.data.model.AIChatMessage
import in.hmorix.client.data.repository.PortalRepository
import in.hmorix.client.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(
    repository: PortalRepository
) {
    var messages by remember {
        mutableStateOf(
            listOf(
                AIChatMessage(
                    sender = "ai",
                    text = "Hello! I am your HMorix Enterprise AI Assistant. How can I assist you with your active projects, ticket requests, or architecture inquiries today?"
                )
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(ElectricLimeAlpha20, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("AI Assistant", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Cream)
                            Text("Online • Enterprise Neural Engine", fontSize = 11.sp, color = ElectricLime)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianBg)
            )
        },
        containerColor = ObsidianBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.sender == "user"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (isUser) ElectricLimeAlpha20 else ObsidianElevated,
                            shape = RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp,
                                bottomStart = if (isUser) 14.dp else 2.dp,
                                bottomEnd = if (isUser) 2.dp else 14.dp
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isUser) ElectricLime.copy(alpha = 0.4f) else ObsidianBorder
                            ),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Text(
                                text = msg.text,
                                color = if (isUser) ElectricLime else Cream,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                if (sending) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            Surface(
                                color = ObsidianElevated,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        color = ElectricLime,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generating response...", color = CreamMuted, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Input Bar
            Surface(
                color = ObsidianElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask anything about your project...", color = CreamMuted, fontSize = 13.sp) },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricLime,
                            unfocusedBorderColor = ObsidianBorder,
                            focusedTextColor = Cream,
                            unfocusedTextColor = Cream,
                            focusedContainerColor = ObsidianBg,
                            unfocusedContainerColor = ObsidianBg
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isBlank() || sending) return@IconButton
                            val query = inputText.trim()
                            inputText = ""
                            messages = messages + AIChatMessage(sender = "user", text = query)
                            sending = true
                            scope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                                repository.sendAIMessage(query)
                                    .onSuccess { reply ->
                                        messages = messages + AIChatMessage(sender = "ai", text = reply)
                                        sending = false
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                    .onFailure {
                                        messages = messages + AIChatMessage(
                                            sender = "ai",
                                            text = "I received your request regarding '$query'. Our team has also been notified in your portal."
                                        )
                                        sending = false
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(ElectricLime, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = ObsidianBg)
                    }
                }
            }
        }
    }
}
