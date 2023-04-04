package com.lexmerciful.delishfood.retrofit

import com.lexmerciful.delishfood.pojo.CategoryList
import com.lexmerciful.delishfood.pojo.MealsByCategoryList
import com.lexmerciful.delishfood.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String):Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategoryList():Call<CategoryList>

    @GET("filter.php")
    fun getCategoryItemsList(@Query("c") categoryName: String):Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchName: String):Call<MealList>
}