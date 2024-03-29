package com.abloom.mery.presentation.ui.profilemenu

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentWebViewFromProfileMenuBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import com.abloom.mery.presentation.ui.webview.WebViewFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFromProfileMenuFragment :
    BaseFragment<FragmentWebViewFromProfileMenuBinding>(R.layout.fragment_web_view_from_profile_menu) {

    private val args: WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appbarWebView.title = getString(args.url.titleId)
        binding.appbarWebView.setOnNavigationClick { findNavController().popBackStack() }
    }
}
