package com.example.recipeorganizer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeorganizer.data.local.entities.Recipe
import com.example.recipeorganizer.domain.model.Resource
import com.example.recipeorganizer.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _randomRecipe = MutableStateFlow<Resource<Recipe>?>(null)
    val randomRecipe: StateFlow<Resource<Recipe>?> = _randomRecipe

    private val _searchResults = MutableStateFlow<Resource<List<Recipe>>?>(null)
    val searchResults: StateFlow<Resource<List<Recipe>>?> = _searchResults

    val bookmarkedRecipes = repository.getBookmarkedRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todaysMealPlans = repository.getMealPlansForDate(getTodayDate())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        getRandomRecipe()
    }

    fun getRandomRecipe() {
        viewModelScope.launch {
            _randomRecipe.value = Resource.Loading()
            _randomRecipe.value = repository.getRandomRecipe()
        }
    }

    fun searchRecipes(query: String) {
        if (query.isBlank()) {
            _searchResults.value = null
            return
        }

        viewModelScope.launch {
            _searchResults.value = Resource.Loading()
            _searchResults.value = repository.searchRecipesByName(query)
        }
    }

    fun clearSearchResults() {
        _searchResults.value = null
    }

    fun toggleBookmark(recipeId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateBookmarkStatus(recipeId, isBookmarked)
        }
    }

    private fun getTodayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
} 