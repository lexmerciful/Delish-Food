package com.lexmerciful.delishfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexmerciful.delishfood.db.MealDatabase
import com.lexmerciful.delishfood.pojo.*
import com.lexmerciful.delishfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealListLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryListLiveData = MutableLiveData<List<Category>>()
    private var favouriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealLiveData = MutableLiveData<List<Meal>>()

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeFragment:", t.message.toString())
            }

        })
    }

    fun getPopularMeal(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null){
                    popularMealListLiveData.value = response.body()!!.meals
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("HomeFragment:", t.message.toString())
            }

        })
    }

    fun getCategoryList(){
        RetrofitInstance.api.getCategoryList().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null){
                    categoryListLiveData.postValue(response.body()!!.categories)
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeFragment:", t.message.toString())
            }

        })
    }

    //Bottom Sheet Call
    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()!!.meals[0]
                meal.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel:", t.message.toString())
            }

        })
    }

    fun getSearchedMeals(searchQuery: String){
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meals = response.body()!!.meals
                meals.let { mealList ->
                    searchedMealLiveData.postValue(mealList)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel:", t.message.toString())
            }

        })
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observerPopularMealListLiveData():LiveData<List<MealsByCategory>>{
        return popularMealListLiveData
    }

    fun observerCategoryListLiveData():LiveData<List<Category>>{
        return categoryListLiveData
    }

    fun observerFavouriteMealLiveData(): LiveData<List<Meal>>{
        return favouriteMealsLiveData
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    fun observerBottomSheetMealLiveData(): LiveData<Meal> = bottomSheetMealLiveData

    fun observerSearchedMealLiveData(): LiveData<List<Meal>> = searchedMealLiveData

}