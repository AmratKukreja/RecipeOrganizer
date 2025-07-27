package com.example.recipeorganizer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "shopping_lists")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    val listId: Long = 0,
    val name: String,
    val dateCreated: Date = Date(),
    val isCompleted: Boolean = false
) 