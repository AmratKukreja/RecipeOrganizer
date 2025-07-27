package com.example.recipeorganizer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("categories")
    val categories: List<CategoryDto>?
)

data class CategoryDto(
    @SerializedName("idCategory")
    val idCategory: String,
    @SerializedName("strCategory")
    val strCategory: String,
    @SerializedName("strCategoryThumb")
    val strCategoryThumb: String,
    @SerializedName("strCategoryDescription")
    val strCategoryDescription: String
) 