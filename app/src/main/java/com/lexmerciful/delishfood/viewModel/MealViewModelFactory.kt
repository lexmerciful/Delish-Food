package com.lexmerciful.delishfood.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lexmerciful.delishfood.db.MealDatabase

class MealViewModelFactory(
    val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //return super.create(modelClass)
        return MealViewModel(mealDatabase) as T
    }
}