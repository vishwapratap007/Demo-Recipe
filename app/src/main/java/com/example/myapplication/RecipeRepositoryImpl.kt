package com.example.myapplication

import com.example.myapplication.api.RecipeApiService
import com.example.myapplication.api.toEntity
import com.example.myapplication.db.RecipeDao
import com.example.myapplication.db.toDomain
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

class RecipeRepositoryImpl @Inject constructor(
    private val recipeApiService: RecipeApiService,
    private val recipeDao: RecipeDao
) : RecipeRepository {
    override fun observeRecipes(): Flow<List<Recipe>> {
        return recipeDao.observeRecipe()
            .map { recipeEntities ->
                recipeEntities.map { recipeEntity ->
                    recipeEntity.toDomain()
                }
            }
    }

    override suspend fun refreshRecipes() {
        flow {
            emit(recipeApiService.getRecipes())
        }.retryWhen { cause, attempt ->
            if (cause is IOException && attempt < 3) {
                delay(1000L * (attempt + 1))
                true
            } else {
                false
            }
        }.collect { response ->
            val recipeEntities = response.recipes.map { it.toEntity() }
            recipeDao.insertRecipes(recipeEntities)
        }
    }
}