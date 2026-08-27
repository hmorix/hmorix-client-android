package in.hmorix.client.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import in.hmorix.client.HMorixApp
import in.hmorix.client.data.repository.PortalRepository
import in.hmorix.client.ui.ai.AIAssistantScreen
import in.hmorix.client.ui.auth.SignInScreen
import in.hmorix.client.ui.auth.SignUpScreen
import in.hmorix.client.ui.invoices.InvoicesScreen
import in.hmorix.client.ui.portal.ClientPortalScreen
import in.hmorix.client.ui.settings.SettingsScreen
import in.hmorix.client.ui.theme.*
import in.hmorix.client.ui.tickets.TicketsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Portal : Screen("portal", "Portal", Icons.Default.Dashboard)
    object Tickets : Screen("tickets", "Tickets", Icons.Default.ConfirmationNumber)
    object Invoices : Screen("invoices", "Invoices", Icons.Default.Receipt)
    object AI : Screen("ai", "AI Assistant", Icons.Default.SmartToy)
    object Settings : Screen("settings", "Account", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    private val repository = PortalRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HMorixTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ObsidianBg
                ) {
                    MainAppNavHost(repository)
                }
            }
        }
    }
}

@Composable
fun MainAppNavHost(repository: PortalRepository) {
    val navController = rememberNavController()
    val sessionManager = HMorixApp.instance.sessionManager
    var isLoggedIn by remember { mutableStateOf(sessionManager.isLoggedIn) }

    if (!isLoggedIn) {
        NavHost(navController = navController, startDestination = "signin") {
            composable("signin") {
                SignInScreen(
                    repository = repository,
                    onLoginSuccess = { isLoggedIn = true },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }
            composable("signup") {
                SignUpScreen(
                    repository = repository,
                    onSignUpSuccess = { isLoggedIn = true },
                    onNavigateToSignIn = { navController.popBackStack() }
                )
            }
        }
    } else {
        val bottomNavItems = listOf(
            Screen.Portal,
            Screen.Tickets,
            Screen.Invoices,
            Screen.AI,
            Screen.Settings
        )

        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBar(
                    containerColor = ObsidianElevated,
                    contentColor = Cream
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, fontSize = 10.sp) },
                            selected = selected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianBg,
                                selectedTextColor = ElectricLime,
                                indicatorColor = ElectricLime,
                                unselectedIconColor = CreamMuted,
                                unselectedTextColor = CreamMuted
                            )
                        )
                    }
                }
            },
            containerColor = ObsidianBg
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Portal.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Portal.route) {
                    ClientPortalScreen(
                        repository = repository,
                        onNavigateToTickets = { navController.navigate(Screen.Tickets.route) },
                        onNavigateToInvoices = { navController.navigate(Screen.Invoices.route) },
                        onNavigateToAI = { navController.navigate(Screen.AI.route) }
                    )
                }
                composable(Screen.Tickets.route) {
                    TicketsScreen(repository = repository)
                }
                composable(Screen.Invoices.route) {
                    InvoicesScreen(repository = repository)
                }
                composable(Screen.AI.route) {
                    AIAssistantScreen(repository = repository)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        repository = repository,
                        onLogout = { isLoggedIn = false }
                    )
                }
            }
        }
    }
}
