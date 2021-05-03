package com.example.foody.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.foody.R
import com.example.foody.databinding.FragmentInstructionsBinding
import com.example.foody.models.Result
import com.example.foody.util.Constants

class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_instructions, container, false)

        val args = arguments
        val bundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        val webView = binding.instructionsWebView
        val sourceUrl =  bundle?.sourceUrl ?: ""

        webView.webViewClient = WebViewClient()
        webView.loadUrl(sourceUrl)

        return binding.root
    }
}