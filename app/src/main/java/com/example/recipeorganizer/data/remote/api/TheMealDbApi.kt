package com.example.recipeorganizer.data.remote.api

import com.example.recipeorganizer.data.remote.dto.CategoryResponse
import com.example.recipeorganizer.data.remote.dto.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {
    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") mealName: String
    ): Response<MealResponse>

    @GET("filter.php")
    suspend fun searchMealByIngredient(
        @Query("i") ingredient: String
    ): Response<MealResponse>

    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>

    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") mealId: String
    ): Response<MealResponse>

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): Response<MealResponse>
} 