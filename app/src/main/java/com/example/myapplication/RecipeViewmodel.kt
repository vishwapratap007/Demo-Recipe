package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.myapplication.api.NetworkConnectivityObserver
import com.example.myapplication.api.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeViewmodel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val networkObserver: NetworkConnectivityObserver
) :
    ViewModel() {

    private var _uiState = MutableStateFlow<RecipeUIState>(RecipeUIState.Empty)
    val uiState = _uiState.asStateFlow()

    init {
        observeRecipes()
        observeNetworkAndSync()
    }

    private fun observeRecipes() {
        viewModelScope.launch {
            recipeRepository.observeRecipes()
                .collect { recipes ->
                    _uiState.update {
                        RecipeUIState.Success(recipes)
                    }
                }
        }
    }

    private fun refreshRecipes() {
        viewModelScope.launch {
            try {
                recipeRepository.refreshRecipes()
            } catch (e: Exception) {
                _uiState.update {
                    RecipeUIState.Error(e)
                }
            }
        }
    }

    private fun observeNetworkAndSync() {
        viewModelScope.launch {
            networkObserver.networkStatus.collect { status ->
                if (status == NetworkStatus.Available) {
                    refreshRecipes()
                }
            }
        }
    }
}