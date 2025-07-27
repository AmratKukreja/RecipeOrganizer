package com.example.recipeorganizer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val id: String,
    val name: String,
    val instructions: String,
    val imageUrl: String?,
    val category: String,
    val area: String,
    val isBookmarked: Boolean = false,
    val dateAdded: Date = Date()
) 