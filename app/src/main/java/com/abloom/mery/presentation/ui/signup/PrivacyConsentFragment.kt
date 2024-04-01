package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentPrivacyConsentBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.ui.webview.WebViewUrl
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyConsentFragment :
    BaseFragment<FragmentPrivacyConsentBinding>(R.layout.fragment_privacy_consent) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindingViewModel()
        initBinding()
        checkAgreeSignUpBtn()
    }

    private fun initBindingViewModel() {
        binding.viewModel = viewModel
    }

    private fun initBinding() {
        binding.allConsentCheckBox.setOnClickListener { onCheckChanged(binding.allConsentCheckBox) }
        binding.subCheckBox1.setOnClickListener { onCheckChanged(binding.subCheckBox1) }
        binding.subCheckBox2.setOnClickListener { onCheckChanged(binding.subCheckBox2) }
        binding.subCheckBox3.setOnClickListener { onCheckChanged(binding.subCheckBox3) }
        binding.subCheckBox4.setOnClickListener { onCheckChanged(binding.subCheckBox4) }
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
            //TODO("뷰모델로 데이터 업데이트")
            findNavController().popBackStack()
        }

    }

    private fun onCheckChanged(checkBox: MaterialCheckBox) {
        if (checkBox.id == R.id.all_consent_check_box) {
            viewModel.allCheckBox.value = checkBox.isChecked
            setSubCheckBox(checkBox.isChecked)
        } else {
            when (checkBox) {
                binding.subCheckBox1 -> viewModel.checkBox1.value = checkBox.isChecked
                binding.subCheckBox2 -> viewModel.checkBox2.value = checkBox.isChecked
                binding.subCheckBox3 -> viewModel.checkBox3.value = checkBox.isChecked
                binding.subCheckBox4 -> viewModel.checkBox4.value = checkBox.isChecked
            }
            viewModel.allCheckBox.value = checkAllSubCheckBox()
        }
        checkAgreeSignUpBtn()
    }

    private fun setSubCheckBox(checked: Boolean) {
        binding.apply {
            viewModel!!.checkBox1.value = checked
            viewModel!!.checkBox2.value = checked
            viewModel!!.checkBox3.value = checked
            viewModel!!.checkBox4.value = checked
        }
    }

    private fun checkAllSubCheckBox() =
        binding.subCheckBox1.isChecked &&
                binding.subCheckBox2.isChecked &&
                binding.subCheckBox3.isChecked &&
                binding.subCheckBox4.isChecked

    private fun checkAgreeSignUpBtn() {
        if (viewModel.allCheckBox.value)
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