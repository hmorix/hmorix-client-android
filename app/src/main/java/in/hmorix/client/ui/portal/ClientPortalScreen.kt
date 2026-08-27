package in.hmorix.client.ui.portal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import in.hmorix.client.R
import in.hmorix.client.data.model.*
import in.hmorix.client.data.repository.PortalRepository
import in.hmorix.client.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPortalScreen(
    repository: PortalRepository,
    onNavigateToTickets: () -> Unit,
    onNavigateToInvoices: () -> Unit,
    onNavigateToAI: () -> Unit
) {
    var portalData by remember { mutableStateOf<PortalData?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val refresh = {
        loading = true
        error = null
        scope.launch {
            repository.fetchPortalData()
                .onSuccess { portalData = it; loading = false }
                .onFailure { error = it.message ?: "Failed to load portal"; loading = false }
        }
    }

    LaunchedEffect(Unit) {
        refresh()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo_hex),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Client Portal",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Cream
                            )
                            Text(
                                text = portalData?.user?.name ?: "Enterprise Dashboard",
                                fontSize = 12.sp,
                                color = ElectricLime
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = ElectricLime)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ObsidianBg)
            )
        },
        containerColor = ObsidianBg
    ) { padding ->
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ElectricLime)
            }
        } else if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = AccentRed, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = error ?: "Error loading portal", color = Cream, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { refresh() },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = ObsidianBg)
                    ) {
                        Text("Try Again")
                    }
                }
            }
        } else {
            val stats = portalData?.stats ?: PortalStats()
            val projects = portalData?.projects ?: emptyList()
            val teams = portalData?.teams ?: emptyList()
            val activities = portalData?.activities ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Metric Summary Cards (2x2 Grid)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                title = "Active Projects",
                                value = stats.activeProjects.toString(),
                                icon = Icons.Default.Folder,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                title = "Open Tickets",
                                value = stats.openTickets.toString(),
                                icon = Icons.Default.ConfirmationNumber,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateToTickets() }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                title = "Invoices Due",
                                value = "₹${stats.invoicesDue.toInt()}",
                                icon = Icons.Default.Receipt,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateToInvoices() }
                            )
                            StatCard(
                                title = "Team Members",
                                value = stats.teamMembers.toString(),
                                icon = Icons.Default.Group,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // AI Quick Assistant Banner
                item {
                    Surface(
                        color = ObsidianElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricLime.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToAI() }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(ElectricLimeAlpha20, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SmartToy, contentDescription = null, tint = ElectricLime)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("HMorix AI Assistant", color = Cream, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Instant AI support, ticket triage & architecture help", color = CreamMuted, fontSize = 12.sp)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ElectricLime)
                        }
                    }
                }

                // Active Projects Section
                item {
                    Text("Active Deliverables & Projects", color = Cream, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                if (projects.isEmpty()) {
                    item {
                        Surface(
                            color = ObsidianElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No active projects found. Contact your dedicated manager to initiate a new deliverable.",
                                color = CreamMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(projects) { project ->
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
                                    Text(project.name, color = Cream, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Surface(
                                        color = ElectricLimeAlpha10,
                                        shape = RoundedCornerShape(4.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricLime.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            project.status.replace("_", " ").uppercase(),
                                            color = ElectricLime,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                if (!project.description.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(project.description, color = CreamMuted, fontSize = 12.sp)
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Progress", color = CreamMuted, fontSize = 11.sp)
                                    Text("${project.progress}%", color = ElectricLime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { project.progress / 100f },
                                    color = ElectricLime,
                                    trackColor = ObsidianCard,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                )
                            }
                        }
                    }
                }

                // Assigned Team Section
                if (teams.isNotEmpty()) {
                    item {
                        Text("Assigned Team Members", color = Cream, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(teams) { member ->
                                Surface(
                                    color = ObsidianElevated,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
                                    modifier = Modifier.width(140.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(ObsidianCard, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                member.name.take(1).uppercase(),
                                                color = ElectricLime,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(member.name, color = Cream, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(member.role, color = CreamMuted, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ObsidianElevated,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Cream, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(title, color = CreamMuted, fontSize = 11.sp)
        }
    }
}
