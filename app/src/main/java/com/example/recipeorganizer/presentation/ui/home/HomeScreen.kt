package com.example.recipeorganizer.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeorganizer.domain.model.Resource
import com.example.recipeorganizer.presentation.ui.components.RecipeCard
import com.example.recipeorganizer.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val randomRecipe by viewModel.randomRecipe.collectAsState()
    val bookmarkedRecipes by viewModel.bookmarkedRecipes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Recipe Organizer",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Random Recipe Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recipe of the Day",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { viewModel.getRandomRecipe() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Get new random recipe")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                when (val resource = randomRecipe) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is Resource.Success -> {
                        resource.data?.let { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = { /* Navigate to recipe details */ },
                                onBookmarkToggle = { 
                                    viewModel.toggleBookmark(recipe.id, !recipe.isBookmarked)
                                }
                            )
                        }
                    }
                    is Resource.Error -> {
                        Text(
                            text = resource.message ?: "Failed to load random recipe",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    null -> {
                        Button(
                            onClick = { viewModel.getRandomRecipe() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Get Random Recipe")
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Bookmarked Recipes Section
        Text(
            text = "Your Bookmarked Recipes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (bookmarkedRecipes.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No bookmarked recipes yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start by searching for recipes in the Search tab and bookmark your favorites",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bookmarkedRecipes.take(5)) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { /* Navigate to recipe details */ },
                        onBookmarkToggle = { 
                            viewModel.toggleBookmark(recipe.id, !recipe.isBookmarked)
                        }
                    )
                }
                
                if (bookmarkedRecipes.size > 5) {
                    item {
                        TextButton(
                            onClick = { /* Navigate to bookmarks tab */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("View All Bookmarked Recipes (${bookmarkedRecipes.size})")
                        }
                    }
                }
            }
        }
    }
} 