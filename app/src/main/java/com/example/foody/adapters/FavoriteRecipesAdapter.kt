package com.example.foody.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.databse.entities.FavoritesEntity
import com.example.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foody.util.GenericDiffUtil

class FavoriteRecipesAdapter(
    private val favoriteRecipesListener: FavoriteRecipesListener,
    private val activity: FragmentActivity
) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.FavoritesRecipesViewHolder>(), ActionMode.Callback {

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class FavoritesRecipesViewHolder(
        private val binding: FavoriteRecipesRowLayoutBinding,
        private val actionModeCallback: ActionMode.Callback
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favoritesEntity: FavoritesEntity,
            favoriteRecipesListener: FavoriteRecipesListener,
            activity: FragmentActivity
        ) {
            binding.favoritesEntity = favoritesEntity
            binding.clickListener = favoriteRecipesListener
            binding.actionModeCallback = actionModeCallback
            binding.activity = activity
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                viewGroup: ViewGroup,
                actionModeCallback: ActionMode.Callback
            ): FavoritesRecipesViewHolder {
                val layoutInflater = LayoutInflater.from(viewGroup.context)
                val binding =
                    FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, viewGroup, false)
                return FavoritesRecipesViewHolder(binding, actionModeCallback)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesRecipesViewHolder {
        return FavoritesRecipesViewHolder.from(parent, this)
    }

    override fun onBindViewHolder(holder: FavoritesRecipesViewHolder, position: Int) {
        val item = favoriteRecipes[position]
        holder.bind(item, favoriteRecipesListener, activity)
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

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        changeStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        changeStatusBarColor(R.color.statusBarColor)
    }

    private fun changeStatusBarColor(color: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, color)
    }
}

class FavoriteRecipesListener(val clickListener: (favoritesEntity: FavoritesEntity) -> Unit) {
    fun onClick(favoritesEntity: FavoritesEntity) = clickListener(favoritesEntity)
}