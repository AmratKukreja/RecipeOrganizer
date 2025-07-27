package com.example.recipeorganizer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeorganizer.data.local.entities.MealPlan
import com.example.recipeorganizer.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Calendar.getInstance().time)
    val selectedDate: StateFlow<Date> = _selectedDate

    val mealPlansForSelectedDate = selectedDate.flatMapLatest { date ->
        repository.getMealPlansForDate(date)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allRecipes = repository.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectDate(date: Date) {
        _selectedDate.value = date
    }

    fun addMealPlan(recipeId: String, date: Date, mealType: String) {
        viewModelScope.launch {
            val mealPlan = MealPlan(
                recipeId = recipeId,
                plannedDate = date,
                mealType = mealType
            )
            repository.insertMealPlan(mealPlan)
        }
    }

    fun removeMealPlan(mealPlan: MealPlan) {
        viewModelScope.launch {
            repository.deleteMealPlan(mealPlan)
        }
    }
} 