package com.example.foody.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.foody.R
import com.example.foody.adapters.IngredientsAdapter
import com.example.foody.databinding.FragmentIngredientsBinding
import com.example.foody.models.Result
import com.example.foody.util.Constants.Companion.RECIPE_RESULT_KEY


class IngredientsFragment : Fragment() {

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
    private lateinit var binding: FragmentIngredientsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false)

        val args = arguments
        val bundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setUpRecyclerView()
        setUpAdapterData(bundle)

        return binding.root
    }

    private fun setUpAdapterData(bundle: Result?) {
        bundle?.extendedIngredients?.let { notNullExtendedIngredients ->
            ingredientsAdapter.setData(
                notNullExtendedIngredients
            )
        }
    }

    private fun setUpRecyclerView() {
        binding.ingredientsRecyclerView.adapter = ingredientsAdapter
    }
}