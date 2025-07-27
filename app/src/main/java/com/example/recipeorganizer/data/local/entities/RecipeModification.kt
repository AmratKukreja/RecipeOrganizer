package com.example.recipeorganizer.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe_modifications",
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
data class RecipeModification(
    @PrimaryKey(autoGenerate = true)
    val modificationId: Long = 0,
    val recipeId: String,
    val originalIngredient: String,
    val substituteIngredient: String,
    val notes: String
) 