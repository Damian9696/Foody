package com.example.foody.binding_adapters

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody.data.databse.entities.FoodJokeEntity
import com.example.foody.models.FoodJoke
import com.example.foody.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {
    companion object {

        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic
        fun setCardAndProgressBarVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> view.visibility = VISIBLE
                        is MaterialCardView -> view.visibility = INVISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> view.visibility = INVISIBLE
                        is MaterialCardView -> {
                            if (database.isNullOrEmpty()) {
                                view.visibility = INVISIBLE
                            } else {
                                view.visibility = VISIBLE
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> view.visibility = INVISIBLE
                        is MaterialCardView -> view.visibility = VISIBLE
                    }
                }
            }
        }

        @BindingAdapter("readApiResponse4", "readDatabase4", requireAll = true)
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if (database.isNullOrEmpty()) {
                view.visibility = VISIBLE
                when (view) {
                    is TextView -> {
                        if (apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }
            if (apiResponse is NetworkResult.Success) {
                view.visibility = INVISIBLE
            }
        }
    }
}