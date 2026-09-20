package com.example.myapplication

sealed interface RecipeUIState {
    object Loading : RecipeUIState
    data class Success(val recipes: List<Recipe>) : RecipeUIState
    data class Error(val exception: Throwable) : RecipeUIState
    data object Empty : RecipeUIState
}