package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.domain.user.model.Authentication
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentPrivacyConsentBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.ui.webview.WebViewUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyConsentFragment :
    BaseFragment<FragmentPrivacyConsentBinding>(R.layout.fragment_privacy_consent) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onWebViewNavigate = ::navigateToWebView
        binding.onSignUpButtonClick = ::handleSignUpButtonClick
    }

    private fun navigateToWebView(url: WebViewUrl) {
        findNavController().navigate(
            SignUpFragmentDirections.actionSignUpFragmentToWebViewFragment(url)
        )
    }

    private fun handleSignUpButtonClick() {
        viewModel.join(Authentication.Kakao("카카오 이메일", "패스워드"))
        findNavController().popBackStack()
    }
}
