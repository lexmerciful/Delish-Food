package com.lexmerciful.delishfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.lexmerciful.delishfood.R
import com.lexmerciful.delishfood.activities.MainActivity
import com.lexmerciful.delishfood.adapters.MealsAdapter
import com.lexmerciful.delishfood.databinding.FragmentSearchBinding
import com.lexmerciful.delishfood.pojo.Meal
import com.lexmerciful.delishfood.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRvAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }

        observeSeachedMealsLiveData()

        var searchJob: Job? = null
        binding.etSearchBox.addTextChangedListener {searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.getSearchedMeals(searchQuery.toString())
            }
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.etSearchBox.text.toString()
        if (searchQuery.isNotEmpty()){
            viewModel.getSearchedMeals(searchQuery)
        }
    }

    private fun observeSeachedMealsLiveData() {
        viewModel.observerSearchedMealLiveData().observe(viewLifecycleOwner, object : Observer<List<Meal>>{
            override fun onChanged(mealsList: List<Meal>?) {
                searchRvAdapter.differ.submitList(mealsList)
            }

        })
    }

    private fun prepareRecyclerView() {
        searchRvAdapter = MealsAdapter()
        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRvAdapter
        }
    }

}