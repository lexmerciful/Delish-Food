package com.lexmerciful.delishfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lexmerciful.delishfood.R
import com.lexmerciful.delishfood.databinding.ActivityMealBinding
import com.lexmerciful.delishfood.db.MealDatabase
import com.lexmerciful.delishfood.fragments.HomeFragment
import com.lexmerciful.delishfood.pojo.Meal
import com.lexmerciful.delishfood.viewModel.MealViewModel
import com.lexmerciful.delishfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var mealViewModel: MealViewModel

    private lateinit var binding: ActivityMealBinding

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealYoutubeLink: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstanceFromDb(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInfoFromIntent()
        setInfoInViews()

        loadingCase()
        mealViewModel.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeImageClick()
        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.fabAddToFav.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)

                Snackbar.make(binding.root, "Added To Favourite", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYoutubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal? = null

    private fun observeMealDetailsLiveData() {
        mealViewModel.observerMealDetails().observe(this, object : Observer<Meal>{
            override fun onChanged(meal: Meal?) {
                onResponseCase()
                mealToSave = meal

                binding.tvCategoryInfo.text = "Category : ${meal!!.strCategory}"
                binding.tvAreaInfo.text = "Area : ${meal.strArea}"
                binding.tvInstructionsSteps.text = meal.strInstructions
                mealYoutubeLink = meal.strYoutube.toString()
            }

        })
    }

    private fun setInfoInViews() {
        Glide
            .with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this,R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this,R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID).toString()
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME).toString()
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB).toString()
    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.fabAddToFav.visibility = View.INVISIBLE
        binding.llMealInfo.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.fabAddToFav.visibility = View.VISIBLE
        binding.llMealInfo.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}