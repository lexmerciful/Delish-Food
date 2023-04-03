package com.lexmerciful.delishfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lexmerciful.delishfood.pojo.MealsByCategory
import com.lexmerciful.delishfood.pojo.MealsByCategoryList
import com.lexmerciful.delishfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel():ViewModel() {

    private var categoryItemsListLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getCategorySelectedItems(categoryName: String){
        RetrofitInstance.api.getCategoryItemsList(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {
                    categoryItemsListLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("Category Activity:", t.message.toString())
            }

        })
    }

    fun observerCategoryItemsListLiveData():LiveData<List<MealsByCategory>>{
        return categoryItemsListLiveData
    }
}