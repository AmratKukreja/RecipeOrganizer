package com.example.recipeorganizer.presentation.ui.shopping

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeorganizer.data.local.entities.ShoppingList
import com.example.recipeorganizer.data.local.entities.ShoppingListItem
import com.example.recipeorganizer.presentation.viewmodel.ShoppingListViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val shoppingLists by viewModel.shoppingLists.collectAsState()
    val currentListItems by viewModel.currentListItems.collectAsState()
    val selectedListId by viewModel.selectedListId.collectAsState()
    
    var showCreateListDialog by remember { mutableStateOf(false) }
    var showAddItemDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shopping Lists",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { showCreateListDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Create new shopping list")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (shoppingLists.isEmpty()) {
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
                        text = "No shopping lists yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Create your first shopping list to get started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showCreateListDialog = true }) {
                        Text("Create Shopping List")
                    }
                }
            }
        } else {
            // Shopping Lists
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(shoppingLists) { shoppingList ->
                    ShoppingListCard(
                        shoppingList = shoppingList,
                        isSelected = selectedListId == shoppingList.listId,
                        onClick = { viewModel.selectShoppingList(shoppingList.listId) },
                        onDelete = { viewModel.deleteShoppingList(shoppingList) }
                    )
                }
                
                selectedListId?.let { listId ->
                    val selectedList = shoppingLists.find { it.listId == listId }
                    selectedList?.let { list ->
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Items in ${list.name}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { showAddItemDialog = true }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add item")
                                }
                            }
                        }
                        
                        if (currentListItems.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "No items in this list yet. Add some items to get started!",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            items(currentListItems) { item ->
                                ShoppingListItemCard(
                                    item = item,
                                    onTogglePurchased = { 
                                        viewModel.toggleItemPurchased(item)
                                    },
                                    onDelete = { /* TODO: Add delete item method to ViewModel */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Create List Dialog
    if (showCreateListDialog) {
        AlertDialog(
            onDismissRequest = { 
                showCreateListDialog = false
                newListName = ""
            },
            title = { Text("Create Shopping List") },
            text = {
                Column {
                    Text("Enter a name for your shopping list:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        label = { Text("List Name") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newListName.isNotBlank()) {
                            viewModel.createShoppingList(newListName.trim())
                            showCreateListDialog = false
                            newListName = ""
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showCreateListDialog = false
                        newListName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Add Item Dialog
    if (showAddItemDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddItemDialog = false
                newItemName = ""
                newItemQuantity = ""
            },
            title = { Text("Add Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Item Name") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newItemQuantity,
                        onValueChange = { newItemQuantity = it },
                        label = { Text("Quantity") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            viewModel.addItemToCurrentList(
                                ingredientName = newItemName.trim(),
                                quantity = newItemQuantity.trim().ifEmpty { "1" },
                                unit = ""
                            )
                            showAddItemDialog = false
                            newItemName = ""
                            newItemQuantity = ""
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showAddItemDialog = false
                        newItemName = ""
                        newItemQuantity = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ShoppingListCard(
    shoppingList: ShoppingList,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (isSelected) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        } else {
            CardDefaults.cardColors()
        },
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = shoppingList.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Created: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(shoppingList.dateCreated)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (shoppingList.isCompleted) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete list")
            }
        }
    }
}

@Composable
private fun ShoppingListItemCard(
    item: ShoppingListItem,
    onTogglePurchased: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isPurchased,
                onCheckedChange = { onTogglePurchased() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.ingredientName,
                    style = MaterialTheme.typography.bodyMedium,
                    textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null,
                    color = if (item.isPurchased) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                if (item.quantity.isNotEmpty()) {
                    Text(
                        text = "Qty: ${item.quantity}${if (item.unit.isNotEmpty()) " ${item.unit}" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete item")
            }
        }
    }
} 