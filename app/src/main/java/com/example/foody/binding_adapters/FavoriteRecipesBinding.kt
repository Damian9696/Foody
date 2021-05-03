package com.example.foody.binding_adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapters.FavoriteRecipesAdapter
import com.example.foody.data.databse.entities.FavoritesEntity

class FavoriteRecipesBinding {
    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(
            view: View,
            favoriteEntities: List<FavoritesEntity>?,
            favoriteRecipesAdapter: FavoriteRecipesAdapter?,
        ) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteEntities.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        favoriteEntities?.let { favoriteRecipesAdapter?.setData(it) }
                    }
                }
                else -> {
                    view.isVisible = favoriteEntities.isNullOrEmpty()
                }
            }
        }
    }
}