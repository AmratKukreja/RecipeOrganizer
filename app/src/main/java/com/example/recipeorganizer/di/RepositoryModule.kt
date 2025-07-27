package com.example.recipeorganizer.di

import com.example.recipeorganizer.data.repository.RecipeRepositoryImpl
import com.example.recipeorganizer.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
} 