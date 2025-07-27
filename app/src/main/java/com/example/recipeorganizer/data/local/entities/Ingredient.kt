package com.example.recipeorganizer.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
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
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Long = 0,
    val name: String,
    val measure: String,
    val recipeId: String
) 