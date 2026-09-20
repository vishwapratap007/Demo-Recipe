package com.example.myapplication.api

import com.example.myapplication.db.RecipeEntity

data class RecipeResponse(
    val recipes: List<RecipeDto>
)

data class RecipeDto(
    val caloriesPerServing: Int,
    val cookTimeMinutes: Int,
    val cuisine: String,
    val difficulty: String,
    val id: Int,
    val image: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val mealType: List<String>,
    val name: String,
    val prepTimeMinutes: Int,
    val rating: Double,
    val reviewCount: Int,
    val servings: Int,
    val tags: List<String>,
    val userId: Int
)

fun RecipeDto.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        name = name,
        caloriesPerServing = caloriesPerServing,
        cookTimeMinutes = cookTimeMinutes,
        cuisine = cuisine,
        difficulty = difficulty,
        image = image,
        ingredients = ingredients,
        instructions = instructions,
        mealType = mealType,
        prepTimeMinutes = prepTimeMinutes,
        rating = rating,
        reviewCount = reviewCount,
        servings = servings,
        tags = tags,
        userId = userId
    )
}