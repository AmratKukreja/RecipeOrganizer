package com.example.recipeorganizer.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = ["listId"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["listId"])]
)
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val listId: Long,
    val ingredientName: String,
    val quantity: String,
    val unit: String,
    val isPurchased: Boolean = false
) 