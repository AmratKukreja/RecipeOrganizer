package com.example.recipeorganizer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeorganizer.data.local.entities.ShoppingList
import com.example.recipeorganizer.data.local.entities.ShoppingListItem
import com.example.recipeorganizer.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    val shoppingLists = repository.getAllShoppingLists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentListItems = MutableStateFlow<List<ShoppingListItem>>(emptyList())
    val currentListItems: StateFlow<List<ShoppingListItem>> = _currentListItems

    private val _selectedListId = MutableStateFlow<Long?>(null)
    val selectedListId: StateFlow<Long?> = _selectedListId

    fun createShoppingList(name: String) {
        viewModelScope.launch {
            val shoppingList = ShoppingList(name = name)
            repository.insertShoppingList(shoppingList)
        }
    }

    fun selectShoppingList(listId: Long) {
        _selectedListId.value = listId
        viewModelScope.launch {
            repository.getShoppingListItems(listId).collect {
                _currentListItems.value = it
            }
        }
    }

    fun addItemToCurrentList(ingredientName: String, quantity: String, unit: String) {
        val listId = _selectedListId.value ?: return
        viewModelScope.launch {
            val item = ShoppingListItem(
                listId = listId,
                ingredientName = ingredientName,
                quantity = quantity,
                unit = unit
            )
            repository.insertShoppingListItem(item)
        }
    }

    fun toggleItemPurchased(item: ShoppingListItem) {
        viewModelScope.launch {
            repository.updateItemPurchasedStatus(item.itemId, !item.isPurchased)
        }
    }

    fun deleteShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            repository.deleteShoppingList(shoppingList)
            if (_selectedListId.value == shoppingList.listId) {
                _selectedListId.value = null
                _currentListItems.value = emptyList()
            }
        }
    }

    fun clearSelectedList() {
        _selectedListId.value = null
        _currentListItems.value = emptyList()
    }
} 