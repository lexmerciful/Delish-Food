package com.lexmerciful.delishfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lexmerciful.delishfood.activities.CategoryActivity
import com.lexmerciful.delishfood.activities.MainActivity
import com.lexmerciful.delishfood.activities.MealActivity
import com.lexmerciful.delishfood.adapters.CategoriesListAdapter
import com.lexmerciful.delishfood.adapters.PopularMealAdapter
import com.lexmerciful.delishfood.databinding.FragmentHomeBinding
import com.lexmerciful.delishfood.fragments.bottomsheets.MealBottomSheetFragment
import com.lexmerciful.delishfood.pojo.Category
import com.lexmerciful.delishfood.pojo.MealsByCategory
import com.lexmerciful.delishfood.pojo.Meal
import com.lexmerciful.delishfood.viewModel.HomeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealAdapter: PopularMealAdapter
    private lateinit var categoriesListAdapter: CategoriesListAdapter


    companion object{
        const val MEAL_ID = "com.lexmerciful.delishfood.fragments.idMeal"
        const val MEAL_NAME = "com.lexmerciful.delishfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.lexmerciful.delishfood.fragments.thumbMeal"
        const val MEAL_CATEGORY = "com.lexmerciful.delishfood.fragments.categoryMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularMealAdapter = PopularMealAdapter()
        categoriesListAdapter = CategoriesListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()
        onRandomMealLongClick()

        viewModel.getPopularMeal()
        observePopularMealListLiveData()
        onPopularItemClick()
        onPopularItemLongClick()

        viewModel.getCategoryList()
        observeCategoryList()
        onCategoryClick()

    }

    private fun onRandomMealLongClick() {
        binding.imgRandomMeal.setOnLongClickListener {
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(randomMeal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
            true
        }
    }

    private fun onPopularItemLongClick() {
        popularMealAdapter.onLongItemClick = {meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesListAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra(MEAL_CATEGORY, category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategoryList() {
        viewModel.observerCategoryListLiveData().observe(viewLifecycleOwner, object : Observer<List<Category>>{
            override fun onChanged(categoryListLiveData: List<Category>?) {
                categoriesListAdapter.setCategoryList(categoryListLiveData!!)
                binding.rvCategory.apply {
                    layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
                    adapter = categoriesListAdapter
                }
            }

        })
    }

    private fun onPopularItemClick() {
        popularMealAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularMealListLiveData() {
        viewModel.observerPopularMealListLiveData().observe(viewLifecycleOwner, object : Observer<List<MealsByCategory>>{
            override fun onChanged(t: List<MealsByCategory>?) {
                val popularMealsList = t

                popularMealAdapter.setMeals(popularMealsList as ArrayList<MealsByCategory>)
                binding.rvMealsPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.rvMealsPopular.adapter = popularMealAdapter
            }

        })
    }

    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->

             randomMeal = meal

            Glide
                .with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.imgRandomMeal)
        }
    }

}