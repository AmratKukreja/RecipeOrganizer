package com.example.recipeorganizer.data.local.dao

import androidx.room.*
import com.example.recipeorganizer.data.local.entities.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    fun getIngredientsForRecipe(recipeId: String): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    fun getIngredientsForRecipeSync(recipeId: String): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: Ingredient): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredients(ingredients: List<Ingredient>): List<Long>

    @Update
    fun updateIngredient(ingredient: Ingredient): Int

    @Delete
    fun deleteIngredient(ingredient: Ingredient): Int

    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId")
    fun deleteIngredientsForRecipe(recipeId: String): Int
} 