package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe ORDER BY name")
    fun observeRecipe(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipe")
    suspend fun getRecipes(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(users: List<RecipeEntity>)

    @Query("DELETE FROM recipe")
    suspend fun deleteAllRecipes()
}