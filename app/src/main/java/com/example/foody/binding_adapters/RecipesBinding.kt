package com.example.foody.binding_adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody.data.databse.RecipesEntity
import com.example.foody.models.FoodRecipe
import com.example.foody.util.NetworkResult

class RecipesBinding {
    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun ImageView.errorImageViewVisibility(
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                this.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Loading) {
                this.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun TextView.errorTextViewVisibility(
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                this.visibility = View.VISIBLE
                this.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading || apiResponse is NetworkResult.Loading) {
                this.visibility = View.INVISIBLE
            }
        }
    }
}