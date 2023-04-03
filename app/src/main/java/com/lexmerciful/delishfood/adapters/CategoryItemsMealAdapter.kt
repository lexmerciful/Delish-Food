package com.lexmerciful.delishfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lexmerciful.delishfood.databinding.MealCardItemBinding
import com.lexmerciful.delishfood.pojo.MealsByCategory

class CategoryItemsMealAdapter():RecyclerView.Adapter<CategoryItemsMealAdapter.CategoryItemsMealViewHolder>() {

    lateinit var onItemClick:((MealsByCategory) -> Unit)
    private var categoryMealList = ArrayList<MealsByCategory>()

    fun setCategoryMealList(categoryMealList: List<MealsByCategory>){
        this.categoryMealList = categoryMealList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    class CategoryItemsMealViewHolder(val binding: MealCardItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemsMealViewHolder {
        return CategoryItemsMealViewHolder(MealCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryMealList.size
    }

    override fun onBindViewHolder(holder: CategoryItemsMealViewHolder, position: Int) {
        val model = categoryMealList[position]

        Glide
            .with(holder.itemView)
            .load(model.strMealThumb)
            .into(holder.binding.imgMeal)

        holder.binding.tvMealName.text = model.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(model)
        }
    }
}