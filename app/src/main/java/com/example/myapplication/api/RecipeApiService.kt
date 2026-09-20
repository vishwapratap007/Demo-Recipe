package com.example.myapplication.api

import retrofit2.http.GET

interface RecipeApiService {

    @GET("recipes")
    suspend fun getRecipes(): RecipeResponse
}