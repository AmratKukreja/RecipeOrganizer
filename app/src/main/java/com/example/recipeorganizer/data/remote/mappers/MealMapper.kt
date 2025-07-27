package com.example.recipeorganizer.data.remote.mappers

import com.example.recipeorganizer.data.local.entities.Ingredient
import com.example.recipeorganizer.data.local.entities.Recipe
import com.example.recipeorganizer.data.remote.dto.MealDto
import java.util.Date

fun MealDto.toRecipe(): Recipe {
    return Recipe(
        id = idMeal,
        name = strMeal,
        instructions = strInstructions,
        imageUrl = strMealThumb,
        category = strCategory,
        area = strArea,
        isBookmarked = false,
        dateAdded = Date()
    )
}

fun MealDto.toIngredients(): List<Ingredient> {
    val ingredients = mutableListOf<Ingredient>()
    
    val ingredientPairs = listOf(
        strIngredient1 to strMeasure1,
        strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5,
        strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7,
        strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11,
        strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13,
        strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15,
        strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17,
        strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19,
        strIngredient20 to strMeasure20
    )
    
    ingredientPairs.forEach { (ingredient, measure) ->
        if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
            ingredients.add(
                Ingredient(
                    name = ingredient.trim(),
                    measure = measure.trim(),
                    recipeId = idMeal
                )
            )
        }
    }
    
    return ingredients
} 