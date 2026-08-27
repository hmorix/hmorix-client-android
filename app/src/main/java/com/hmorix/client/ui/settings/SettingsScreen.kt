package com.hmorix.client.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmorix.client.HMorixApp
import com.hmorix.client.R
import com.hmorix.client.data.repository.PortalRepository
import com.hmorix.client.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    repository: PortalRepository,
    onLogout: () -> Unit
) {
    val sessionManager = HMorixApp.instance.sessionManager
    val user = sessionManager.currentUser
    var serverUrl by remember { mutableStateOf(sessionManager.apiBaseUrl) }
    var showServerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account & Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Cream) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianBg)
            )
        },
        containerColor = ObsidianBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Card
            item {
                Surface(
                    color = ObsidianElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(ElectricLimeAlpha20, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (user?.name ?: user?.email ?: "C").take(1).uppercase(),
                                color = ElectricLime,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(user?.name ?: "Valued Client", color = Cream, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(user?.email ?: "", color = CreamMuted, fontSize = 12.sp)
                            Surface(
                                color = ElectricLimeAlpha10,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = (user?.role ?: "CLIENT").uppercase(),
                                    color = ElectricLime,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Settings Options
            item {
                Text("Connection & Configuration", color = Cream, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            item {
                Surface(
                    color = ObsidianElevated,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ListItem(
                            headlineContent = { Text("API Server Endpoint", color = Cream, fontSize = 14.sp) },
                            supportingContent = { Text(serverUrl, color = CreamMuted, fontSize = 12.sp) },
                            leadingContent = { Icon(Icons.Default.Dns, contentDescription = null, tint = ElectricLime) },
                            trailingContent = {
                                TextButton(onClick = { showServerDialog = true }) {
                                    Text("Change", color = ElectricLime)
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = ObsidianElevated)
                        )

                        HorizontalDivider(color = ObsidianBorder)

                        ListItem(
                            headlineContent = { Text("Cyber Theme", color = Cream, fontSize = 14.sp) },
                            supportingContent = { Text("Obsidian & Electric Lime (Active)", color = CreamMuted, fontSize = 12.sp) },
                            leadingContent = { Icon(Icons.Default.Palette, contentDescription = null, tint = ElectricLime) },
                            colors = ListItemDefaults.colors(containerColor = ObsidianElevated)
                        )

                        HorizontalDivider(color = ObsidianBorder)

                        ListItem(
                            headlineContent = { Text("Client App Version", color = Cream, fontSize = 14.sp) },
                            supportingContent = { Text("v1.0.0 (Production Architecture)", color = CreamMuted, fontSize = 12.sp) },
                            leadingContent = { Icon(Icons.Default.Info, contentDescription = null, tint = ElectricLime) },
                            colors = ListItemDefaults.colors(containerColor = ObsidianElevated)
                        )
                    }
                }
            }

            // Logout Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        repository.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed.copy(alpha = 0.15f), contentColor = AccentRed),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Server URL Dialog
        if (showServerDialog) {
            var tempUrl by remember { mutableStateOf(serverUrl) }
            AlertDialog(
                onDismissRequest = { showServerDialog = false },
                title = { Text("Configure API Server", color = Cream) },
                text = {
                    Column {
                        Text("Enter your custom HMorix backend server URL:", color = CreamMuted, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tempUrl,
                            onValueChange = { tempUrl = it },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricLime,
                                unfocusedBorderColor = ObsidianBorder,
                                focusedTextColor = Cream,
                                unfocusedTextColor = Cream
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            serverUrl = tempUrl.trim()
                            sessionManager.apiBaseUrl = serverUrl
                            showServerDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = ObsidianBg)
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showServerDialog = false }) {
                        Text("Cancel", color = CreamMuted)
                    }
                },
                containerColor = ObsidianCard
            )
        }
    }
}
