package com.example.foody.binding_adapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foody.R
import com.example.foody.models.Result
import com.example.foody.ui.fragments.recipes.RecipesFragmentDirections

class RecipesRowBinding {
    companion object {

        @BindingAdapter("onRecipesClickListener")
        @JvmStatic
        fun ConstraintLayout.onRecipesClickListener(result: Result) {
            this.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(
                        result
                    )
                    this.findNavController().navigate(action)
                } catch (exception: Exception) {
                    Log.d(
                        "RecipesRowBinding",
                        "onRecipesClickListener: ${exception.message}"
                    )
                }
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumbersOfLikes(textView: TextView, likes: Int) {
            textView.text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun setNumberOfMinutes(textView: TextView, minutes: Int) {
            textView.text = minutes.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, isVegan: Boolean) {
            if (isVegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFormUrl(imageView: ImageView, url: String) {
            imageView.load(url) {
                crossfade(600)
                error(R.drawable.ic_placeholder_error)
            }
        }

    }
}