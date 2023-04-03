package com.lexmerciful.delishfood.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.lexmerciful.delishfood.adapters.CategoryItemsMealAdapter
import com.lexmerciful.delishfood.databinding.ActivityCategoryBinding
import com.lexmerciful.delishfood.fragments.HomeFragment
import com.lexmerciful.delishfood.pojo.MealsByCategory
import com.lexmerciful.delishfood.viewModel.CategoryViewModel

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryName: String
    private lateinit var categoryItemsMealAdapter: CategoryItemsMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryViewModel = ViewModelProviders.of(this)[CategoryViewModel::class.java]
        categoryItemsMealAdapter = CategoryItemsMealAdapter()

        getCategoryMealsFromIntent()
        categoryViewModel.getCategorySelectedItems(categoryName)
        observeCategoryMealItemsLiveData()
        onCategoryMealItemClick()
    }

    private fun onCategoryMealItemClick() {
        categoryItemsMealAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeCategoryMealItemsLiveData() {
        categoryViewModel.observerCategoryItemsListLiveData().observe(this, object : Observer<List<MealsByCategory>>{
            override fun onChanged(categoryMealItemsList: List<MealsByCategory>?) {
                categoryItemsMealAdapter.setCategoryMealList(categoryMealItemsList!!)

                binding.tvCategoryCount.text = "$categoryName : ${categoryMealItemsList.size}"
                binding.rvCategoryItems.apply {
                    layoutManager = GridLayoutManager(this@CategoryActivity, 2, GridLayoutManager.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = categoryItemsMealAdapter
                }
            }

        })
    }

    private fun getCategoryMealsFromIntent() {
        val intent = intent
        categoryName = intent.getStringExtra(HomeFragment.MEAL_CATEGORY).toString()
    }
}