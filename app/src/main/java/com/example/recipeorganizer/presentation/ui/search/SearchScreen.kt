package com.example.recipeorganizer.presentation.ui.search

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
import com.example.recipeorganizer.domain.model.Resource
import com.example.recipeorganizer.presentation.ui.components.RecipeCard
import com.example.recipeorganizer.presentation.ui.components.SearchBar
import com.example.recipeorganizer.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val searchResults by viewModel.searchResults.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Auto-search with delay when user types
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            delay(500) // Wait 500ms before searching
            viewModel.searchRecipes(searchQuery)
        } else {
            viewModel.clearSearchResults()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search Recipes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { 
                if (it.isNotBlank()) {
                    viewModel.searchRecipes(it)
                }
            },
            onClear = { 
                searchQuery = ""
                viewModel.clearSearchResults()
            },
            placeholder = "Search for recipes (e.g., chicken, pasta, cake)..."
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (val resource = searchResults) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Searching for \"$searchQuery\"...")
                    }
                }
            }
            is Resource.Success -> {
                if (resource.data.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No recipes found for \"$searchQuery\"",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Try searching for something else like 'chicken', 'pasta', or 'dessert'",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Found ${resource.data.size} recipe(s) for \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(resource.data) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = { /* Navigate to recipe details */ },
                                onBookmarkToggle = { 
                                    viewModel.toggleBookmark(recipe.id, !recipe.isBookmarked)
                                }
                            )
                        }
                    }
                }
            }
            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error searching for recipes",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = resource.message ?: "An error occurred",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { 
                                if (searchQuery.isNotBlank()) {
                                    viewModel.searchRecipes(searchQuery)
                                }
                            }
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔍 Start searching for recipes!",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Try searching for 'chicken', 'pasta', 'dessert', or any ingredient",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
} 