package com.example.foody.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.databse.entities.FavoritesEntity
import com.example.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foody.ui.fragments.favorite.FavoriteRecipesFragmentDirections
import com.example.foody.util.GenericDiffUtil

class FavoriteRecipesAdapter(
    private val activity: FragmentActivity
) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.FavoritesRecipesViewHolder>(), ActionMode.Callback {

    private var favoriteRecipes = emptyList<FavoritesEntity>()
    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var favoritesRecipesViewHolders = arrayListOf<FavoritesRecipesViewHolder>()
    private lateinit var actionMode: ActionMode

    class FavoritesRecipesViewHolder(
        val binding: FavoriteRecipesRowLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favoritesEntity: FavoritesEntity
        ) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                viewGroup: ViewGroup
            ): FavoritesRecipesViewHolder {
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

        onClick(holder, item)
        onLongClick(holder, item)
    }

    private fun applySelection(holder: FavoritesRecipesViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackground, R.color.strokeColor)
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimaryDark)
        }
        applyActionModeTitle()
    }

    private fun changeRecipeStyle(
        holder: FavoritesRecipesViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.binding.backgroundFavoriteRecipes.setBackgroundColor(
            ContextCompat.getColor(activity, backgroundColor)
        )
        holder.binding.favouriteCardViewRecipesRow.strokeColor =
            ContextCompat.getColor(activity, strokeColor)
    }

    private fun onLongClick(holder: FavoritesRecipesViewHolder, item: FavoritesEntity) {
        holder.binding.favoriteRecipesRow.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                activity.startActionMode(this)
                applySelection(holder, item)
                applySelectedViewHolders(holder)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    private fun onClick(
        holder: FavoritesRecipesViewHolder,
        item: FavoritesEntity
    ) {
        holder.binding.favoriteRecipesRow.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, item)
                applySelectedViewHolders(holder)
            } else {
                navigateToRecipe(holder, item)
            }
        }
    }

    private fun applySelectedViewHolders(holder: FavoritesRecipesViewHolder) {
        if (favoritesRecipesViewHolders.contains(holder)) {
            favoritesRecipesViewHolders.remove(holder)
        } else {
            favoritesRecipesViewHolders.add(holder)
        }
    }

    private fun navigateToRecipe(
        holder: FavoritesRecipesViewHolder,
        item: FavoritesEntity
    ) {
        holder.itemView.findNavController().navigate(
            FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                item.result
            )
        )
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

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> actionMode.finish()
            1 -> actionMode.title = "${selectedRecipes.size} item selected"
            else -> actionMode.title = "${selectedRecipes.size} items selected"
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        actionMode = mode!!
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
        favoritesRecipesViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackground, R.color.strokeColor)
        }
        favoritesRecipesViewHolders.clear()
        multiSelection = false
        selectedRecipes.clear()
        changeStatusBarColor(R.color.statusBarColor)
    }

    private fun changeStatusBarColor(color: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, color)
    }
}