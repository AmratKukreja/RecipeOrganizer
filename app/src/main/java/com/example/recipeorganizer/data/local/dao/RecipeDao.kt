package com.example.recipeorganizer.data.local.dao

import androidx.room.*
import com.example.recipeorganizer.data.local.entities.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY dateAdded DESC")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE isBookmarked = 1 ORDER BY dateAdded DESC")
    fun getBookmarkedRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: String): Recipe?

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchRecipes(searchQuery: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY name ASC")
    fun getRecipesByCategory(category: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<Recipe>): List<Long>

    @Update
    fun updateRecipe(recipe: Recipe): Int

    @Query("UPDATE recipes SET isBookmarked = :isBookmarked WHERE id = :id")
    fun updateBookmarkStatus(id: String, isBookmarked: Boolean): Int

    @Delete
    fun deleteRecipe(recipe: Recipe): Int

    @Query("DELETE FROM recipes WHERE id = :id")
    fun deleteRecipeById(id: String): Int

    @Query("SELECT COUNT(*) FROM recipes")
    fun getRecipeCount(): Int
} 