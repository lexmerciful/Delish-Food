package com.lexmerciful.delishfood.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lexmerciful.delishfood.pojo.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>
}