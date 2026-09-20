package com.example.myapplication

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun observeRecipes(): Flow<List<Recipe>>

    suspend fun refreshRecipes()
}