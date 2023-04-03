package com.lexmerciful.delishfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexmerciful.delishfood.databinding.PopularItemsBinding
import com.lexmerciful.delishfood.pojo.MealsByCategory

class PopularMealAdapter():RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick:((MealsByCategory) -> Unit)
    private var mealsList = ArrayList<MealsByCategory>()

    fun setMeals(mealsList: ArrayList<MealsByCategory>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    class PopularMealViewHolder(val binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide
            .with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMeal)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }
}