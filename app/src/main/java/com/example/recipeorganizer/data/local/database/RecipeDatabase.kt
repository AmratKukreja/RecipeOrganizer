package com.example.recipeorganizer.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.recipeorganizer.data.local.Converters
import com.example.recipeorganizer.data.local.dao.*
import com.example.recipeorganizer.data.local.entities.*

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
        ShoppingList::class,
        ShoppingListItem::class,
        MealPlan::class,
        RecipeModification::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun recipeModificationDao(): RecipeModificationDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 