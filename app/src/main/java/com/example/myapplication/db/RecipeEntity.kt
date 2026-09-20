package com.example.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.Recipe
import kotlin.Int

@Entity(tableName = "recipe")
data class RecipeEntity(

    @PrimaryKey
    val id: Int,
    val caloriesPerServing: Int,
    val cookTimeMinutes: Int,
    val cuisine: String,
    val difficulty: String,
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

fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
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