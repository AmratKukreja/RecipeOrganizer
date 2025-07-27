package com.example.recipeorganizer.di

import android.content.Context
import androidx.room.Room
import com.example.recipeorganizer.data.local.dao.*
import com.example.recipeorganizer.data.local.database.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeDatabase::class.java,
            "recipe_database"
        ).build()
    }

    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideIngredientDao(database: RecipeDatabase): IngredientDao {
        return database.ingredientDao()
    }

    @Provides
    fun provideShoppingListDao(database: RecipeDatabase): ShoppingListDao {
        return database.shoppingListDao()
    }

    @Provides
    fun provideMealPlanDao(database: RecipeDatabase): MealPlanDao {
        return database.mealPlanDao()
    }

    @Provides
    fun provideRecipeModificationDao(database: RecipeDatabase): RecipeModificationDao {
        return database.recipeModificationDao()
    }
} 