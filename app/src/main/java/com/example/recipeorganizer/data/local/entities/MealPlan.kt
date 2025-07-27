package com.example.recipeorganizer.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "meal_plans",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["recipeId"])]
)
data class MealPlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Long = 0,
    val recipeId: String,
    val plannedDate: Date,
    val mealType: String // breakfast, lunch, dinner, snack
) 