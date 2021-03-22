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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private lateinit var binding: ActivityDetailsBinding
    private val mainViewModel: MainViewModel by viewModels()

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

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        val viewPager = binding.viewPager
        viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val saveToFavouritesItem = menu?.findItem(R.id.save_to_favourites)
        changeMenuItemColorIfRecipeIsSaved(saveToFavouritesItem)
        return true
    }

    private fun changeMenuItemColorIfRecipeIsSaved(saveToFavouritesItem: MenuItem?) {
        mainViewModel.readFavoriteRecipes.observe(this) { nullableListOfFavourites ->
            nullableListOfFavourites?.let { notNullListOfFavourites ->
                for (savedRecipes in notNullListOfFavourites) {
                    if (savedRecipes.result.id == args.result.id) {
                        saveToFavouritesItem?.let { notNullItem ->
                            changeMenuItemColor(notNullItem, R.color.yellow)
                        }
                        break
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites) {
            saveToFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouritesEntity =
            FavoritesEntity(
                result = args.result
            )
        mainViewModel.insertFavoriteRecipe(favouritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar(getString(R.string.recipe_saved_as_favourite))
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
}