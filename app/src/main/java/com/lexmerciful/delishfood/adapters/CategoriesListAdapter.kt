package com.lexmerciful.delishfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.lexmerciful.delishfood.databinding.CategoryItemBinding
import com.lexmerciful.delishfood.pojo.Category

class CategoriesListAdapter():RecyclerView.Adapter<CategoriesListAdapter.CategoryViewHolder>() {

    private var categoryList = ArrayList<Category>()
    lateinit var onItemClick:((Category) -> Unit)

    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val model = categoryList[position]
        Glide
            .with(holder.itemView)
            .load(model.strCategoryThumb)
            .into(holder.binding.imgCategory)

        holder.binding.tvCategoryName.text = model.strCategory

        holder.itemView.setOnClickListener {
            onItemClick.invoke(model)
        }
    }
}