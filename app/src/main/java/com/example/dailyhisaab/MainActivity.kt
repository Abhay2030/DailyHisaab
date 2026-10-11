package com.example.dailyhisaab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dailyhisaab.database.DatabaseHelper
import com.example.dailyhisaab.ui.screens.HistoryScreen
import com.example.dailyhisaab.ui.screens.InsightsScreen
import com.example.dailyhisaab.viewmodel.HistoryViewModel
import com.example.dailyhisaab.viewmodel.HistoryViewModelFactory
import com.example.dailyhisaab.ui.screens.AddTransactionScreen
import com.example.dailyhisaab.ui.screens.HomeScreen
import com.example.dailyhisaab.ui.theme.DailyHisaabTheme
import com.example.dailyhisaab.utils.SettingsManager
import com.example.dailyhisaab.viewmodel.AddTransactionViewModel
import com.example.dailyhisaab.viewmodel.AddTransactionViewModelFactory
import com.example.dailyhisaab.viewmodel.HomeViewModel
import com.example.dailyhisaab.viewmodel.HomeViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        dbHelper = DatabaseHelper(this)
        settingsManager = SettingsManager(this)

        setContent {
            DailyHisaabTheme {
                MainAppScreen(dbHelper, settingsManager)
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Transactions : Screen("transactions", "Hisaab", Icons.Default.List)
    object Add : Screen("add", "Add", Icons.Default.Add)
    object Budgets : Screen("budgets", "Budgets", Icons.Default.DateRange)
    object Insights : Screen("insights", "Insights", Icons.Default.Settings) // Will change icon later
}

@Composable
fun MainAppScreen(dbHelper: DatabaseHelper, settingsManager: SettingsManager) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Home,
        Screen.Transactions,
        Screen.Add,
        Screen.Budgets,
        Screen.Insights
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            // Only show bottom bar on main screens
            val isMainScreen = screens.any { it.route == currentDestination?.route }
            
            if (isMainScreen) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                if (screen == Screen.Add) {
                                    navController.navigate("add_transaction") // special route for add
                                } else {
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
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val factory = HomeViewModelFactory(dbHelper, settingsManager)
                val viewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToAddTransaction = { navController.navigate("add_transaction") },
                    onNavigateToHistory = { navController.navigate(Screen.Transactions.route) },
                    onNavigateToTransactionDetail = { id -> navController.navigate("add_transaction?expense_id=$id") }
                )
            }
            composable(Screen.Transactions.route) {
                val factory = HistoryViewModelFactory(dbHelper)
                val viewModel: HistoryViewModel = viewModel(factory = factory)
                HistoryScreen(
                    viewModel = viewModel,
                    onNavigateToTransactionDetail = { id -> navController.navigate("add_transaction?expense_id=$id") }
                )
            }
            composable(Screen.Budgets.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Budgets Screen (Coming Soon)")
                }
            }
            composable(Screen.Insights.route) {
                val factory = HomeViewModelFactory(dbHelper, settingsManager)
                val viewModel: HomeViewModel = viewModel(factory = factory)
                InsightsScreen(viewModel = viewModel)
            }
            composable(
                route = "add_transaction?expense_id={expense_id}",
                arguments = listOf(navArgument("expense_id") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { backStackEntry ->
                val expenseId = backStackEntry.arguments?.getLong("expense_id") ?: -1L
                val factory = AddTransactionViewModelFactory(dbHelper, settingsManager, expenseId)
                val viewModel: AddTransactionViewModel = viewModel(factory = factory)
                
                AddTransactionScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
