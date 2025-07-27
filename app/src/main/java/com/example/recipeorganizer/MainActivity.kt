package com.example.recipeorganizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeorganizer.presentation.ui.home.HomeScreen
import com.example.recipeorganizer.presentation.ui.search.SearchScreen
import com.example.recipeorganizer.presentation.ui.bookmarks.BookmarksScreen
import com.example.recipeorganizer.presentation.ui.mealplan.MealPlanScreen
import com.example.recipeorganizer.presentation.ui.shopping.ShoppingListScreen
import com.example.recipeorganizer.ui.theme.RecipeOrganizerTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object Bookmarks : Screen("bookmarks", "Bookmarks", Icons.Default.Favorite)
    object MealPlan : Screen("meal_plan", "Meal Plan", Icons.Default.DateRange)
    object Shopping : Screen("shopping", "Shopping", Icons.Default.ShoppingCart)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeOrganizerTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Bookmarks,
        Screen.MealPlan,
        Screen.Shopping
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
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
                HomeScreen(navController = navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController = navController)
            }
            composable(Screen.Bookmarks.route) {
                BookmarksScreen(navController = navController)
            }
            composable(Screen.MealPlan.route) {
                MealPlanScreen(navController = navController)
            }
            composable(Screen.Shopping.route) {
                ShoppingListScreen(navController = navController)
            }
        }
    }
}