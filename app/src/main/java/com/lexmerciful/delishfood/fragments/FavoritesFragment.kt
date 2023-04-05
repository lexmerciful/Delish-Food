package com.lexmerciful.delishfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lexmerciful.delishfood.activities.MainActivity
import com.lexmerciful.delishfood.activities.MealActivity
import com.lexmerciful.delishfood.adapters.MealsAdapter
import com.lexmerciful.delishfood.databinding.FragmentFavoritesBinding
import com.lexmerciful.delishfood.viewModel.HomeViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mealsAdapter: MealsAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper.SimpleCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavouritesMeals()
        prepareRecyclerView()
        onMealItemClick()

        swipeToDeleteAndUndo()
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavourite)
    }

    private fun swipeToDeleteAndUndo() {
        itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = mealsAdapter.differ.currentList[position]
                viewModel.deleteMeal(deletedMeal)

                Snackbar.make(requireView(), "Meal Deleted Successfully", Snackbar.LENGTH_LONG)
                    .setAction("Undo",
                        View.OnClickListener {
                            viewModel.insertMeal(deletedMeal)
                        }).show()
            }

        }
    }

    private fun onMealItemClick() {
        mealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        mealsAdapter = MealsAdapter()
        binding.rvFavourite.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter

        }
    }

    private fun observeFavouritesMeals() {
        viewModel.observerFavouriteMealLiveData().observe(viewLifecycleOwner, Observer { meals ->
            if (meals.isEmpty()){
                binding.tvNoFavouriteMeal.visibility = View.VISIBLE
                binding.rvFavourite.visibility = View.GONE
            }else{
                binding.tvNoFavouriteMeal.visibility = View.GONE
                binding.rvFavourite.visibility = View.VISIBLE
            }
            mealsAdapter.differ.submitList(meals)

        })
    }

}