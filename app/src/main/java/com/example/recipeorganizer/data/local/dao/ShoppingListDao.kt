package com.example.recipeorganizer.data.local.dao

import androidx.room.*
import com.example.recipeorganizer.data.local.entities.ShoppingList
import com.example.recipeorganizer.data.local.entities.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists ORDER BY dateCreated DESC")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_lists WHERE listId = :listId")
    fun getShoppingListById(listId: Long): ShoppingList?

    @Query("SELECT * FROM shopping_list_items WHERE listId = :listId")
    fun getShoppingListItems(listId: Long): Flow<List<ShoppingListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoppingList(shoppingList: ShoppingList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoppingListItem(item: ShoppingListItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoppingListItems(items: List<ShoppingListItem>): List<Long>

    @Update
    fun updateShoppingList(shoppingList: ShoppingList): Int

    @Update
    fun updateShoppingListItem(item: ShoppingListItem): Int

    @Query("UPDATE shopping_list_items SET isPurchased = :isPurchased WHERE itemId = :itemId")
    fun updateItemPurchasedStatus(itemId: Long, isPurchased: Boolean): Int

    @Delete
    fun deleteShoppingList(shoppingList: ShoppingList): Int

    @Delete
    fun deleteShoppingListItem(item: ShoppingListItem): Int

    @Query("DELETE FROM shopping_list_items WHERE listId = :listId")
    fun deleteAllItemsFromList(listId: Long): Int
} 