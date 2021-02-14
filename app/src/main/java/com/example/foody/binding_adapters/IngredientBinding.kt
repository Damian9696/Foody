package com.example.foody.binding_adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.foody.models.ExtendedIngredient
import com.example.foody.util.Constants.Companion.BASE_IMAGE_URL
import java.util.*

class IngredientBinding {
    companion object {

        @JvmStatic
        @BindingAdapter("setIngredientImage")
        fun ImageView.setIngredientImage(extendedIngredient: ExtendedIngredient?) {
            extendedIngredient?.let { notNullExtendedIngredient ->
                this.load(BASE_IMAGE_URL + notNullExtendedIngredient.image)
            }
        }

        @JvmStatic
        @BindingAdapter("setIngredientName")
        fun TextView.setIngredientName(extendedIngredient: ExtendedIngredient?) {
            extendedIngredient?.let { notNullExtendedIngredient ->
                this.text = notNullExtendedIngredient.name.capitalize(Locale.ROOT)
            }
        }

        @JvmStatic
        @BindingAdapter("setIngredientAmount")
        fun TextView.setIngredientAmount(extendedIngredient: ExtendedIngredient?) {
            extendedIngredient?.let { notNullExtendedIngredient ->
                this.text = notNullExtendedIngredient.amount.toString()
            }
        }

        @JvmStatic
        @BindingAdapter("setIngredientUnit")
        fun TextView.setIngredientUnit(extendedIngredient: ExtendedIngredient?) {
            extendedIngredient?.let { notNullExtendedIngredient ->
                this.text = notNullExtendedIngredient.unit.capitalize(Locale.ROOT)
            }
        }

        @JvmStatic
        @BindingAdapter("setIngredientConsistency")
        fun TextView.setIngredientConsistency(extendedIngredient: ExtendedIngredient?) {
            extendedIngredient?.let { notNullExtendedIngredient ->
                this.text = notNullExtendedIngredient.consistency.capitalize(Locale.ROOT)
            }
        }

        @JvmStatic
        @BindingAdapter("setIngredientOriginal")
        fun TextView.setIngredientOriginal(extendedIngredient: ExtendedIngredient?) {
            extendedIngredient?.let { notNullExtendedIngredient ->
                this.text = notNullExtendedIngredient.original.capitalize(Locale.ROOT)
            }
        }

    }
}