package com.example.foody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foody.view_models.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.extensions.observeOnce
import com.example.foody.util.NetworkListener
import com.example.foody.util.NetworkResult
import com.example.foody.view_models.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val TAG = "RecipesFragment"

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mAdapter by lazy { RecipesAdapter() }
    private val args by navArgs<RecipesFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { notNullActivity ->
            mainViewModel = ViewModelProvider(notNullActivity).get(MainViewModel::class.java)
            recipesViewModel = ViewModelProvider(notNullActivity).get(RecipesViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binding the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            context?.let { notNullContext ->
                networkListener.checkInternetAvailability(notNullContext)
                    .collect { status ->
                        recipesViewModel.networkStatus = status
                        recipesViewModel.showNetworkStatus()
                        readDatabase()
                    }
            }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRecipes.adapter = mAdapter
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { notNullQuery ->
            searchApiData(notNullQuery)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d(TAG, "readDatabase called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d(TAG, "requestApiData called!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        context,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        context,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.recyclerViewRecipes.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerViewRecipes.hideShimmer()
    }

}