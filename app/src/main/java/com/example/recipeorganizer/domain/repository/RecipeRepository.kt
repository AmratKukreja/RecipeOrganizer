package com.example.recipeorganizer.domain.repository

import com.example.recipeorganizer.data.local.entities.*
import com.example.recipeorganizer.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface RecipeRepository {
    // Recipe operations
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getBookmarkedRecipes(): Flow<List<Recipe>>
    suspend fun getRecipeById(id: String): Recipe?
    fun searchRecipes(query: String): Flow<List<Recipe>>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun updateBookmarkStatus(id: String, isBookmarked: Boolean)
    suspend fun deleteRecipe(recipe: Recipe)
    
    // API operations
    suspend fun searchRecipesByName(name: String): Resource<List<Recipe>>
    suspend fun searchRecipesByIngredient(ingredient: String): Resource<List<Recipe>>
    suspend fun getRandomRecipe(): Resource<Recipe>
    suspend fun getRecipeDetailsById(id: String): Resource<Recipe>
    
    // Ingredient operations
    fun getIngredientsForRecipe(recipeId: String): Flow<List<Ingredient>>
    suspend fun insertIngredients(ingredients: List<Ingredient>)
    
    // Shopping list operations
    fun getAllShoppingLists(): Flow<List<ShoppingList>>
    fun getShoppingListItems(listId: Long): Flow<List<ShoppingListItem>>
    suspend fun insertShoppingList(shoppingList: ShoppingList): Long
    suspend fun insertShoppingListItem(item: ShoppingListItem)
    suspend fun updateItemPurchasedStatus(itemId: Long, isPurchased: Boolean)
    suspend fun deleteShoppingList(shoppingList: ShoppingList)
    
    // Meal plan operations
    fun getAllMealPlans(): Flow<List<MealPlan>>
    fun getMealPlansForDateRange(startDate: Date, endDate: Date): Flow<List<MealPlan>>
    fun getMealPlansForDate(date: Date): Flow<List<MealPlan>>
    suspend fun insertMealPlan(mealPlan: MealPlan)
    suspend fun deleteMealPlan(mealPlan: MealPlan)
    
    // Recipe modification operations
    fun getModificationsForRecipe(recipeId: String): Flow<List<RecipeModification>>
    suspend fun insertModification(modification: RecipeModification)
    suspend fun deleteModification(modification: RecipeModification)
} 