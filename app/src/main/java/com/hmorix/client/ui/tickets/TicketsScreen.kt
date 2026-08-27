package com.hmorix.client.ui.tickets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmorix.client.data.model.Ticket
import com.hmorix.client.data.repository.PortalRepository
import com.hmorix.client.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsScreen(
    repository: PortalRepository
) {
    var tickets by remember { mutableStateOf<List<Ticket>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("medium") }
    var submitting by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val refresh = {
        loading = true
        scope.launch {
            repository.fetchPortalData()
                .onSuccess {
                    tickets = it.tickets
                    loading = false
                }
                .onFailure {
                    loading = false
                }
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Support & Tickets", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Cream)
                },
                actions = {
                    IconButton(onClick = { refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = ElectricLime)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianBg)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = ElectricLime,
                contentColor = ObsidianBg
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Ticket")
            }
        },
        containerColor = ObsidianBg
    ) { padding ->
        if (loading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ElectricLime)
            }
        } else if (tickets.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = CreamMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No Tickets Submitted", color = Cream, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Need help or technical changes? Tap + to submit a ticket.", color = CreamMuted, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets) { ticket ->
                    val statusColor = when (ticket.status.lowercase()) {
                        "open" -> ElectricLime
                        "in_progress" -> AccentBlue
                        "resolved", "closed" -> AccentGreen
                        else -> CreamMuted
                    }

                    Surface(
                        color = ObsidianElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(ticket.subject, color = Cream, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                                Surface(
                                    color = statusColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        ticket.status.uppercase(),
                                        color = statusColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(ticket.description, color = CreamMuted, fontSize = 13.sp)

                            if (!ticket.createdAt.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = CreamSubtle, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(ticket.createdAt, color = CreamSubtle, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Create Ticket Dialog
        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Submit Support Ticket", color = Cream, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (errorMsg != null) {
                            Text(errorMsg ?: "", color = AccentRed, fontSize = 12.sp)
                        }

                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Subject", color = CreamMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricLime,
                                unfocusedBorderColor = ObsidianBorder,
                                focusedTextColor = Cream,
                                unfocusedTextColor = Cream
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description", color = CreamMuted) },
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricLime,
                                unfocusedBorderColor = ObsidianBorder,
                                focusedTextColor = Cream,
                                unfocusedTextColor = Cream
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("low", "medium", "high").forEach { p ->
                                val selected = priority == p
                                Surface(
                                    color = if (selected) ElectricLimeAlpha20 else ObsidianElevated,
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) ElectricLime else ObsidianBorder),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    TextButton(onClick = { priority = p }) {
                                        Text(p.uppercase(), color = if (selected) ElectricLime else CreamMuted, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (subject.isBlank() || description.isBlank()) {
                                errorMsg = "Please fill in all fields"
                                return@Button
                            }
                            submitting = true
                            scope.launch {
                                repository.submitTicket(subject.trim(), description.trim(), priority)
                                    .onSuccess {
                                        submitting = false
                                        showCreateDialog = false
                                        subject = ""
                                        description = ""
                                        refresh()
                                    }
                                    .onFailure {
                                        submitting = false
                                        errorMsg = it.message ?: "Failed to submit"
                                    }
                            }
                        },
                        enabled = !submitting,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = ObsidianBg)
                    ) {
                        Text(if (submitting) "Submitting..." else "Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel", color = CreamMuted)
                    }
                },
                containerColor = ObsidianCard
            )
        }
    }
}
