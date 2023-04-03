package com.lexmerciful.delishfood.fragments.bottomsheets

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lexmerciful.delishfood.R
import com.lexmerciful.delishfood.activities.MainActivity
import com.lexmerciful.delishfood.activities.MealActivity
import com.lexmerciful.delishfood.databinding.FragmentMealBottomSheetBinding
import com.lexmerciful.delishfood.fragments.HomeFragment
import com.lexmerciful.delishfood.viewModel.HomeViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MEAL_ID = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [MealBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null){
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(HomeFragment.MEAL_ID, mealId)
                intent.putExtra(HomeFragment.MEAL_NAME, mealName)
                intent.putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                startActivity(intent)
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null

    private fun observeBottomSheetMeal() {
        viewModel.observerBottomSheetMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            Glide
                .with(this)
                .load(meal.strMealThumb)
                .into(binding.imgBottomSheet)

            binding.tvBtmSheetMealName.text = meal.strMeal
            binding.tvBtmSheetCategory.text = meal.strCategory
            binding.tvBtmSheetArea.text = meal.strArea

            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param param1 Parameter 1.
         * @return A new instance of fragment MealBottomSheetFragment.
         */
        @JvmStatic
        fun newInstance(selectedMealId: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, selectedMealId)
                }
            }
    }
}