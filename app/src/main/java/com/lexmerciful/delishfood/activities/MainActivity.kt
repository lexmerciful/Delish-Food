package com.lexmerciful.delishfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lexmerciful.delishfood.R
import com.lexmerciful.delishfood.db.MealDatabase
import com.lexmerciful.delishfood.viewModel.HomeViewModel
import com.lexmerciful.delishfood.viewModel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstanceFromDb(this)
        val viewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, viewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btmNav = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(btmNav, navController)
    }
}