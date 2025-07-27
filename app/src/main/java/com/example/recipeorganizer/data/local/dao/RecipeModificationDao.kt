package com.example.recipeorganizer.data.local.dao

import androidx.room.*
import com.example.recipeorganizer.data.local.entities.RecipeModification
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeModificationDao {
    @Query("SELECT * FROM recipe_modifications WHERE recipeId = :recipeId")
    fun getModificationsForRecipe(recipeId: String): Flow<List<RecipeModification>>

    @Query("SELECT * FROM recipe_modifications WHERE recipeId = :recipeId")
    fun getModificationsForRecipeSync(recipeId: String): List<RecipeModification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModification(modification: RecipeModification): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModifications(modifications: List<RecipeModification>): List<Long>

    @Update
    fun updateModification(modification: RecipeModification): Int

    @Delete
    fun deleteModification(modification: RecipeModification): Int

    @Query("DELETE FROM recipe_modifications WHERE recipeId = :recipeId")
    fun deleteModificationsForRecipe(recipeId: String): Int
} 