package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.domain.user.model.Authentication
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentPrivacyConsentBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.ui.webview.WebViewUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyConsentFragment :
    BaseFragment<FragmentPrivacyConsentBinding>(R.layout.fragment_privacy_consent) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindingViewModel()
        initBinding()
        observeAllCheckBox()
    }

    private fun initBindingViewModel() {
        binding.viewModel = viewModel
    }

    private fun initBinding() {
        binding.allConsentCheckBox.setOnClickListener {
            setSubCheckBoxesValue()
        }
        binding.subCheckBox1.setOnClickListener {
            viewModel.subCheckBox1.value = binding.subCheckBox1.isChecked
        }
        binding.subCheckBox2.setOnClickListener {
            viewModel.subCheckBox2.value = binding.subCheckBox2.isChecked
        }
        binding.subCheckBox3.setOnClickListener {
            viewModel.subCheckBox3.value = binding.subCheckBox3.isChecked
        }
        binding.subCheckBox4.setOnClickListener {
            viewModel.subCheckBox4.value = binding.subCheckBox4.isChecked
        }
        binding.webTermsOfUseDetail.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToWebViewFragment(WebViewUrl.TERMS_OF_USE)
            )
        }
        binding.webSensitivePrivacyDetail.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToWebViewFragment(WebViewUrl.SENSITIVE_PRIVACY)
            )
        }
        binding.webPrivacyPolicyDetail.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToWebViewFragment(WebViewUrl.PRIVACY_POLICY)
            )
        }
        binding.agreeSignupBtn.setOnClickListener {
            viewModel.join(Authentication.Kakao("카카오 이메일", "패스워드"))
            findNavController().popBackStack()
        }
    }

    private fun observeAllCheckBox() {
        repeatOnStarted {
            viewModel.allCheckBox.collect { isChecked ->
                checkAgreeSignUpBtn(isChecked)
            }
        }
    }

    private fun setSubCheckBoxesValue() {
        val allConsentCheckBoxChecked = binding.allConsentCheckBox.isChecked

        viewModel.apply {
            subCheckBox1.value = allConsentCheckBoxChecked
            subCheckBox2.value = allConsentCheckBoxChecked
            subCheckBox3.value = allConsentCheckBoxChecked
            subCheckBox4.value = allConsentCheckBoxChecked
        }
    }

    private fun checkAgreeSignUpBtn(check: Boolean) {
        if (check)
            activeAgreeSignUpBtn()
        else
            inActiveAgreeSignUpBtn()
    }

    private fun activeAgreeSignUpBtn() {
        binding.agreeSignupBtn.isEnabled = true
    }

    private fun inActiveAgreeSignUpBtn() {
        binding.agreeSignupBtn.isEnabled = false
    }
}