package com.example.recipeorganizer.presentation.ui.mealplan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeorganizer.presentation.viewmodel.MealPlanViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MealPlanScreen(
    navController: NavController,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val mealPlansForDate by viewModel.mealPlansForSelectedDate.collectAsState()
    val allRecipes by viewModel.allRecipes.collectAsState()
    
    var showAddMealDialog by remember { mutableStateOf(false) }
    var selectedMealType by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Meal Plan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Date Selector
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Selected Date",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(selectedDate),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { /* TODO: Open date picker */ }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Meal Types
        val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mealTypes) { mealType ->
                MealTypeCard(
                    mealType = mealType,
                    mealPlans = mealPlansForDate.filter { it.mealType == mealType },
                    allRecipes = allRecipes,
                    onAddMeal = {
                        selectedMealType = mealType
                        showAddMealDialog = true
                    },
                    onRemoveMeal = { mealPlan ->
                        viewModel.removeMealPlan(mealPlan)
                    }
                )
            }
        }
    }
    
    // Add Meal Dialog
    if (showAddMealDialog && allRecipes.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { showAddMealDialog = false },
            title = { Text("Add $selectedMealType") },
            text = {
                LazyColumn {
                    items(allRecipes) { recipe ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.addMealPlan(recipe.id, selectedDate, selectedMealType)
                                    showAddMealDialog = false
                                }
                        ) {
                            Text(
                                text = recipe.name,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddMealDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    } else if (showAddMealDialog && allRecipes.isEmpty()) {
        AlertDialog(
            onDismissRequest = { showAddMealDialog = false },
            title = { Text("No Recipes Available") },
            text = { Text("Please search and save some recipes first to add them to your meal plan.") },
            confirmButton = {
                TextButton(onClick = { showAddMealDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun MealTypeCard(
    mealType: String,
    mealPlans: List<com.example.recipeorganizer.data.local.entities.MealPlan>,
    allRecipes: List<com.example.recipeorganizer.data.local.entities.Recipe>,
    onAddMeal: () -> Unit,
    onRemoveMeal: (com.example.recipeorganizer.data.local.entities.MealPlan) -> Unit
) {
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
                    text = mealType,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onAddMeal) {
                    Icon(Icons.Default.Add, contentDescription = "Add $mealType")
                }
            }
            
            if (mealPlans.isEmpty()) {
                Text(
                    text = "No $mealType planned",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                mealPlans.forEach { mealPlan ->
                    val recipe = allRecipes.find { it.id == mealPlan.recipeId }
                    if (recipe != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = recipe.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = recipe.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextButton(onClick = { onRemoveMeal(mealPlan) }) {
                                Text("Remove")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
} 