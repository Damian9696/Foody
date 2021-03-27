package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.data.databse.entities.FavoritesEntity
import com.example.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foody.util.GenericDiffUtil

class FavoriteRecipesAdapter :
    RecyclerView.Adapter<FavoriteRecipesAdapter.FavoritesRecipesViewHolder>() {

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class FavoritesRecipesViewHolder(private val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(viewGroup: ViewGroup): FavoritesRecipesViewHolder {
                val layoutInflater = LayoutInflater.from(viewGroup.context)
                val binding =
                    FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, viewGroup, false)
                return FavoritesRecipesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesRecipesViewHolder {
        return FavoritesRecipesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoritesRecipesViewHolder, position: Int) {
        val item = favoriteRecipes[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtils = GenericDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtils)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

}