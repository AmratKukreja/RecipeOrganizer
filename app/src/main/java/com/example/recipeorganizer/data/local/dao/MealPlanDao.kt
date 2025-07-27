package com.example.recipeorganizer.data.local.dao

import androidx.room.*
import com.example.recipeorganizer.data.local.entities.MealPlan
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM meal_plans ORDER BY plannedDate ASC")
    fun getAllMealPlans(): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plans WHERE plannedDate BETWEEN :startDate AND :endDate ORDER BY plannedDate ASC")
    fun getMealPlansForDateRange(startDate: Date, endDate: Date): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plans WHERE plannedDate = :date ORDER BY mealType ASC")
    fun getMealPlansForDate(date: Date): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plans WHERE recipeId = :recipeId")
    fun getMealPlansForRecipe(recipeId: String): Flow<List<MealPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealPlan(mealPlan: MealPlan): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealPlans(mealPlans: List<MealPlan>): List<Long>

    @Update
    fun updateMealPlan(mealPlan: MealPlan): Int

    @Delete
    fun deleteMealPlan(mealPlan: MealPlan): Int

    @Query("DELETE FROM meal_plans WHERE planId = :planId")
    fun deleteMealPlanById(planId: Long): Int

    @Query("DELETE FROM meal_plans WHERE recipeId = :recipeId")
    fun deleteMealPlansForRecipe(recipeId: String): Int
} 