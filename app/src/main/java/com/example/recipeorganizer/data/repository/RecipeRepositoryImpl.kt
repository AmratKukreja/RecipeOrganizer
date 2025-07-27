package com.example.recipeorganizer.data.repository

import com.example.recipeorganizer.data.local.dao.*
import com.example.recipeorganizer.data.local.entities.*
import com.example.recipeorganizer.data.remote.api.TheMealDbApi
import com.example.recipeorganizer.data.remote.mappers.toIngredients
import com.example.recipeorganizer.data.remote.mappers.toRecipe
import com.example.recipeorganizer.domain.model.Resource
import com.example.recipeorganizer.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao,
    private val shoppingListDao: ShoppingListDao,
    private val mealPlanDao: MealPlanDao,
    private val recipeModificationDao: RecipeModificationDao,
    private val api: TheMealDbApi
) : RecipeRepository {

    // Recipe operations
    override fun getAllRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    override fun getBookmarkedRecipes(): Flow<List<Recipe>> = recipeDao.getBookmarkedRecipes()

    override suspend fun getRecipeById(id: String): Recipe? = withContext(Dispatchers.IO) {
        recipeDao.getRecipeById(id)
    }

    override fun searchRecipes(query: String): Flow<List<Recipe>> = recipeDao.searchRecipes(query)

    override suspend fun insertRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeDao.insertRecipe(recipe)
        Unit
    }

    override suspend fun updateBookmarkStatus(id: String, isBookmarked: Boolean) = withContext(Dispatchers.IO) {
        recipeDao.updateBookmarkStatus(id, isBookmarked)
        Unit
    }

    override suspend fun deleteRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeDao.deleteRecipe(recipe)
        Unit
    }

    // API operations
    override suspend fun searchRecipesByName(name: String): Resource<List<Recipe>> {
        return try {
            val response = api.searchMealByName(name)
            if (response.isSuccessful && response.body() != null) {
                val meals = response.body()!!.meals
                if (meals.isNullOrEmpty()) {
                    Resource.Error("No recipes found")
                } else {
                    val recipes = meals.map { it.toRecipe() }
                    // Cache recipes locally
                    withContext(Dispatchers.IO) {
                        recipeDao.insertRecipes(recipes)
                        // Cache ingredients
                        meals.forEach { meal ->
                            val ingredients = meal.toIngredients()
                            if (ingredients.isNotEmpty()) {
                                ingredientDao.insertIngredients(ingredients)
                            }
                        }
                    }
                    Resource.Success(recipes)
                }
            } else {
                Resource.Error("Failed to search recipes: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun searchRecipesByIngredient(ingredient: String): Resource<List<Recipe>> {
        return try {
            val response = api.searchMealByIngredient(ingredient)
            if (response.isSuccessful && response.body() != null) {
                val meals = response.body()!!.meals
                if (meals.isNullOrEmpty()) {
                    Resource.Error("No recipes found with this ingredient")
                } else {
                    val recipes = meals.map { it.toRecipe() }
                    Resource.Success(recipes)
                }
            } else {
                Resource.Error("Failed to search by ingredient: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getRandomRecipe(): Resource<Recipe> {
        return try {
            val response = api.getRandomMeal()
            if (response.isSuccessful && response.body() != null) {
                val meal = response.body()!!.meals?.firstOrNull()
                if (meal != null) {
                    val recipe = meal.toRecipe()
                    val ingredients = meal.toIngredients()
                    
                    // Cache locally
                    withContext(Dispatchers.IO) {
                        recipeDao.insertRecipe(recipe)
                        if (ingredients.isNotEmpty()) {
                            ingredientDao.insertIngredients(ingredients)
                        }
                    }
                    
                    Resource.Success(recipe)
                } else {
                    Resource.Error("No random recipe available")
                }
            } else {
                Resource.Error("Failed to get random recipe: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getRecipeDetailsById(id: String): Resource<Recipe> {
        return try {
            val response = api.getMealById(id)
            if (response.isSuccessful && response.body() != null) {
                val meal = response.body()!!.meals?.firstOrNull()
                if (meal != null) {
                    val recipe = meal.toRecipe()
                    val ingredients = meal.toIngredients()
                    
                    // Cache locally
                    withContext(Dispatchers.IO) {
                        recipeDao.insertRecipe(recipe)
                        if (ingredients.isNotEmpty()) {
                            ingredientDao.insertIngredients(ingredients)
                        }
                    }
                    
                    Resource.Success(recipe)
                } else {
                    Resource.Error("Recipe not found")
                }
            } else {
                Resource.Error("Failed to get recipe details: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Network error: ${e.localizedMessage}")
        }
    }

    // Ingredient operations
    override fun getIngredientsForRecipe(recipeId: String): Flow<List<Ingredient>> =
        ingredientDao.getIngredientsForRecipe(recipeId)

    override suspend fun insertIngredients(ingredients: List<Ingredient>) = withContext(Dispatchers.IO) {
        ingredientDao.insertIngredients(ingredients)
        Unit
    }

    // Shopping list operations
    override fun getAllShoppingLists(): Flow<List<ShoppingList>> =
        shoppingListDao.getAllShoppingLists()

    override fun getShoppingListItems(listId: Long): Flow<List<ShoppingListItem>> =
        shoppingListDao.getShoppingListItems(listId)

    override suspend fun insertShoppingList(shoppingList: ShoppingList): Long = withContext(Dispatchers.IO) {
        shoppingListDao.insertShoppingList(shoppingList)
    }

    override suspend fun insertShoppingListItem(item: ShoppingListItem) = withContext(Dispatchers.IO) {
        shoppingListDao.insertShoppingListItem(item)
        Unit
    }

    override suspend fun updateItemPurchasedStatus(itemId: Long, isPurchased: Boolean) = withContext(Dispatchers.IO) {
        shoppingListDao.updateItemPurchasedStatus(itemId, isPurchased)
        Unit
    }

    override suspend fun deleteShoppingList(shoppingList: ShoppingList) = withContext(Dispatchers.IO) {
        shoppingListDao.deleteShoppingList(shoppingList)
        Unit
    }

    // Meal plan operations
    override fun getAllMealPlans(): Flow<List<MealPlan>> = mealPlanDao.getAllMealPlans()

    override fun getMealPlansForDateRange(startDate: Date, endDate: Date): Flow<List<MealPlan>> =
        mealPlanDao.getMealPlansForDateRange(startDate, endDate)

    override fun getMealPlansForDate(date: Date): Flow<List<MealPlan>> =
        mealPlanDao.getMealPlansForDate(date)

    override suspend fun insertMealPlan(mealPlan: MealPlan) = withContext(Dispatchers.IO) {
        mealPlanDao.insertMealPlan(mealPlan)
        Unit
    }

    override suspend fun deleteMealPlan(mealPlan: MealPlan) = withContext(Dispatchers.IO) {
        mealPlanDao.deleteMealPlan(mealPlan)
        Unit
    }

    // Recipe modification operations
    override fun getModificationsForRecipe(recipeId: String): Flow<List<RecipeModification>> =
        recipeModificationDao.getModificationsForRecipe(recipeId)

    override suspend fun insertModification(modification: RecipeModification) = withContext(Dispatchers.IO) {
        recipeModificationDao.insertModification(modification)
        Unit
    }

    override suspend fun deleteModification(modification: RecipeModification) = withContext(Dispatchers.IO) {
        recipeModificationDao.deleteModification(modification)
        Unit
    }
} 