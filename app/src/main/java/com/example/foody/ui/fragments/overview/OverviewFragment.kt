package com.example.foody.ui.fragments.overview

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.foody.R
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.models.Result

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overview, container, false)

        val args = arguments
        val bundle: Result? = args?.getParcelable("resultBundle")

        binding.mainImageView.load(bundle?.image)
        binding.titleTextView.text = bundle?.title
        binding.likesTextView.text = bundle?.aggregateLikes.toString()
        binding.timeTextView.text = bundle?.readyInMinutes.toString()
        binding.summaryTextView.text = bundle?.summary

        context?.let { notNullContext ->
            if (bundle?.vegetarian == true) {

                binding.vegetarianImageView.setColorFilter(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
                binding.vegetarianTextView.setTextColor(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
            }

            if (bundle?.vegan == true) {
                binding.veganImageView.setColorFilter(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
                binding.vegetarianTextView.setTextColor(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
            }

            if (bundle?.glutenFree == true) {
                binding.glutenFreeImageView.setColorFilter(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
                binding.glutenFreeTextView.setTextColor(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
            }

            if (bundle?.dairyFree == true) {
                binding.dairyFreeImageView.setColorFilter(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
                binding.dairyFreeTextView.setTextColor(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
            }

            if (bundle?.veryHealthy == true) {
                binding.healthyImageView.setColorFilter(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
                binding.healthyTextView.setTextColor(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
            }

            if (bundle?.cheap == true) {
                binding.cheapImageView.setColorFilter(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
                binding.cheapTextView.setTextColor(
                    ContextCompat.getColor(
                        notNullContext,
                        R.color.green
                    )
                )
            }
        }

        return binding.root
    }
}