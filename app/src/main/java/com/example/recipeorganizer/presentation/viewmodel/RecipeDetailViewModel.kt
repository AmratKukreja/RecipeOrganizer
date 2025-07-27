package com.example.recipeorganizer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeorganizer.data.local.entities.*
import com.example.recipeorganizer.domain.model.Resource
import com.example.recipeorganizer.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _recipeResource = MutableStateFlow<Resource<Recipe>?>(null)
    val recipeResource: StateFlow<Resource<Recipe>?> = _recipeResource

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients

    private val _modifications = MutableStateFlow<List<RecipeModification>>(emptyList())
    val modifications: StateFlow<List<RecipeModification>> = _modifications

    fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            // First try to get from local database
            val localRecipe = repository.getRecipeById(recipeId)
            if (localRecipe != null) {
                _recipe.value = localRecipe
                loadIngredientsAndModifications(recipeId)
            } else {
                // If not found locally, fetch from API
                _recipeResource.value = Resource.Loading()
                val apiResult = repository.getRecipeDetailsById(recipeId)
                _recipeResource.value = apiResult
                if (apiResult is Resource.Success) {
                    _recipe.value = apiResult.data
                    loadIngredientsAndModifications(recipeId)
                }
            }
        }
    }

    private fun loadIngredientsAndModifications(recipeId: String) {
        viewModelScope.launch {
            repository.getIngredientsForRecipe(recipeId).collect {
                _ingredients.value = it
            }
        }
        
        viewModelScope.launch {
            repository.getModificationsForRecipe(recipeId).collect {
                _modifications.value = it
            }
        }
    }

    fun toggleBookmark() {
        val currentRecipe = _recipe.value ?: return
        viewModelScope.launch {
            val newBookmarkStatus = !currentRecipe.isBookmarked
            repository.updateBookmarkStatus(currentRecipe.id, newBookmarkStatus)
            _recipe.value = currentRecipe.copy(isBookmarked = newBookmarkStatus)
        }
    }

    fun addToMealPlan(date: Date, mealType: String) {
        val currentRecipe = _recipe.value ?: return
        viewModelScope.launch {
            val mealPlan = MealPlan(
                recipeId = currentRecipe.id,
                plannedDate = date,
                mealType = mealType
            )
            repository.insertMealPlan(mealPlan)
        }
    }

    fun addIngredientsToShoppingList(shoppingListId: Long) {
        viewModelScope.launch {
            val currentIngredients = _ingredients.value
            currentIngredients.forEach { ingredient ->
                val shoppingItem = ShoppingListItem(
                    listId = shoppingListId,
                    ingredientName = ingredient.name,
                    quantity = ingredient.measure,
                    unit = ""
                )
                repository.insertShoppingListItem(shoppingItem)
            }
        }
    }

    fun addModification(originalIngredient: String, substituteIngredient: String, notes: String) {
        val currentRecipe = _recipe.value ?: return
        viewModelScope.launch {
            val modification = RecipeModification(
                recipeId = currentRecipe.id,
                originalIngredient = originalIngredient,
                substituteIngredient = substituteIngredient,
                notes = notes
            )
            repository.insertModification(modification)
        }
    }

    fun deleteModification(modification: RecipeModification) {
        viewModelScope.launch {
            repository.deleteModification(modification)
        }
    }
} 