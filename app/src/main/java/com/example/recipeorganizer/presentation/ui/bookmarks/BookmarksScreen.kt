package com.example.recipeorganizer.presentation.ui.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeorganizer.presentation.ui.components.RecipeCard
import com.example.recipeorganizer.presentation.viewmodel.HomeViewModel

@Composable
fun BookmarksScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val bookmarkedRecipes by viewModel.bookmarkedRecipes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Bookmarked Recipes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (bookmarkedRecipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No bookmarked recipes yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start bookmarking recipes to see them here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarkedRecipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { /* Navigate to recipe details */ },
                        onBookmarkToggle = { 
                            viewModel.toggleBookmark(recipe.id, false)
                        }
                    )
                }
            }
        }
    }
} 