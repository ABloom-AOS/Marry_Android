package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.BuildConfig
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentPrivacyConsentBinding
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.ui.webview.WebViewUrl
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyConsentFragment :
    NavigationFragment<FragmentPrivacyConsentBinding>(R.layout.fragment_privacy_consent) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    private lateinit var mp: MixpanelAPI

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        mp = MixpanelAPI.getInstance(requireContext(), BuildConfig.MIX_PANEL_TOKEN, false)
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onWebViewNavigate = ::navigateToWebView
        binding.onSignUpButtonClick = ::handleSignUpButtonClick
    }

    private fun navigateToWebView(url: WebViewUrl) {
        findNavController().navigateSafely(
            SignUpFragmentDirections.actionSignUpFragmentToWebViewFragment(url)
        )
    }

    private fun handleSignUpButtonClick() {
        mp.track("signup_complete")
        viewModel.join()
        findNavController().popBackStack()
    }
}
