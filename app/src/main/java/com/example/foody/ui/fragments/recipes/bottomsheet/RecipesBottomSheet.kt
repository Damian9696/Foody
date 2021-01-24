package com.example.foody.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.foody.R
import com.example.foody.databinding.RecipesBottomSheetBinding
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.view_models.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var binding: RecipesBottomSheetBinding

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0

    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let { notNullActivity ->
            recipesViewModel = ViewModelProvider(notNullActivity).get(RecipesViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.recipes_bottom_sheet, container, false)
        binding.recipesBottomSheet = this

        recipesViewModel.readMealAndDietType.asLiveData()
            .observe(viewLifecycleOwner) { values ->
                values?.let { notNullValues ->
                    mealTypeChip = notNullValues.selectedMealType
                    mealTypeChipId = notNullValues.selectedMealTypeId

                    dietTypeChip = notNullValues.selectedDietType
                    dietTypeChipId = notNullValues.selectedDietTypeId

                    updateChip(mealTypeChipId, binding.mealTypeChipGroup)
                    updateChip(dietTypeChipId, binding.dietTypeChipGroup)
                }
            }

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)

            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)

            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", "updateChip: ${e.message}")
            }
        }
    }

    fun applyMealAndDietType() {
        recipesViewModel.saveMealAndDietType(
            mealTypeChip,
            mealTypeChipId,
            dietTypeChip,
            dietTypeChipId
        )
    }
}