package com.abloom.mery.presentation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentAnnouncementBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.view.setOnNavigationClick

class AnnouncementFragment :
    BaseFragment<FragmentAnnouncementBinding>(R.layout.fragment_announcement) {

    private val args: AnnouncementFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppBar()
        setupWebView()
    }

    private fun setupAppBar() {
        binding.appbarWebView.setOnNavigationClick { findNavController().popBackStack() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl(args.url)
        }
    }
}
