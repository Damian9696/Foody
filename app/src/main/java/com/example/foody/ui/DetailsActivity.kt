package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.databse.entities.FavoritesEntity
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.ui.fragments.ingredients.IngredientsFragment
import com.example.foody.ui.fragments.instructions.InstructionsFragment
import com.example.foody.ui.fragments.overview.OverviewFragment
import com.example.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foody.view_models.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private lateinit var binding: ActivityDetailsBinding
    private val mainViewModel: MainViewModel by viewModels()

    private var isRecipesSaved = false
    private var savedRecipeId = 0

    private lateinit var saveToFavouritesItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        val toolbar = binding.toolbar
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add(getString(R.string.overview))
        titles.add(getString(R.string.ingredients))
        titles.add(getString(R.string.instructions))

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menu?.let {
            saveToFavouritesItem = it.findItem(R.id.save_to_favourites)
            changeMenuItemColorIfRecipeIsSaved(saveToFavouritesItem)
        }
        return true
    }

    private fun changeMenuItemColorIfRecipeIsSaved(saveToFavouritesItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { nullableListOfFavourites ->
            nullableListOfFavourites?.let { notNullListOfFavourites ->
                for (savedRecipes in notNullListOfFavourites) {
                    if (savedRecipes.result.id == args.result.id) {
                        changeMenuItemColor(saveToFavouritesItem, R.color.yellow)
                        isRecipesSaved = true
                        savedRecipeId = savedRecipes.id
                        break
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites && !isRecipesSaved) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.save_to_favourites && isRecipesSaved) {
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouritesEntity =
            FavoritesEntity(
                id = 0,
                result = args.result
            )
        mainViewModel.insertFavoriteRecipe(favouritesEntity)
        isRecipesSaved = true
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar(getString(R.string.recipe_saved_as_favourite))
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favouritesEntity =
            FavoritesEntity(
                id = savedRecipeId,
                result = args.result
            )
        mainViewModel.deleteFavoriteRecipe(favouritesEntity)
        isRecipesSaved = false
        changeMenuItemColor(item, R.color.white)
        showSnackBar(getString(R.string.recipes_removed_from_favourites))
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction(getString(R.string.okey)) {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::saveToFavouritesItem.isInitialized) {
            changeMenuItemColor(saveToFavouritesItem, R.color.yellow)
        }
    }
}