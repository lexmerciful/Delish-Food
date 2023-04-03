package com.lexmerciful.delishfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexmerciful.delishfood.databinding.MealCardItemBinding
import com.lexmerciful.delishfood.pojo.Meal

class FavouriteMealsAdapter(): RecyclerView.Adapter<FavouriteMealsAdapter.FavouriteMealsViewHolder>() {

    lateinit var onItemClick:((Meal) -> Unit)

    inner class FavouriteMealsViewHolder(val binding: MealCardItemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealsViewHolder {
        return FavouriteMealsViewHolder(
            MealCardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavouriteMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]

        Glide
            .with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.tvMealName.text = meal.strMeal.toString()

        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }
}