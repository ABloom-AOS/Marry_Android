package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentPrivacyConsentBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyConsentFragment :
    BaseFragment<FragmentPrivacyConsentBinding>(R.layout.fragment_privacy_consent) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        checkAgreeSignUpBtn()
    }

    private fun initBinding() {
        binding.allConsentCheckBox.setOnClickListener { onCheckChanged(binding.allConsentCheckBox) }
        binding.subCheckBox1.setOnClickListener { onCheckChanged(binding.subCheckBox1) }
        binding.subCheckBox2.setOnClickListener { onCheckChanged(binding.subCheckBox2) }
        binding.subCheckBox3.setOnClickListener { onCheckChanged(binding.subCheckBox3) }
        binding.subCheckBox4.setOnClickListener { onCheckChanged(binding.subCheckBox4) }
        binding.webTermsOfUseDetail.setOnClickListener {  }
        binding.webSensitivePrivacyDetail.setOnClickListener {  }
        binding.webPrivacyPolicyDetail.setOnClickListener {  }
        binding.agreeSignupBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onCheckChanged(compoundButton: CompoundButton) {
        if (compoundButton.id == R.id.all_consent_check_box)
            setSubCheckBox(binding.allConsentCheckBox.isChecked)
        else
            binding.allConsentCheckBox.isChecked = checkAllSubCheckBox()

        checkAgreeSignUpBtn()
    }

    private fun setSubCheckBox(checked: Boolean) {
        binding.apply {
            subCheckBox1.isChecked = checked
            subCheckBox2.isChecked = checked
            subCheckBox3.isChecked = checked
            subCheckBox4.isChecked = checked
        }
    }

    private fun checkAllSubCheckBox() =
        binding.subCheckBox1.isChecked &&
                binding.subCheckBox2.isChecked &&
                binding.subCheckBox3.isChecked &&
                binding.subCheckBox4.isChecked

    private fun checkAgreeSignUpBtn() {
        if (binding.allConsentCheckBox.isChecked)
            activeAgreeSignUpBtn()
        else
            inActiveAgreeSignUpBtn()
    }

    private fun activeAgreeSignUpBtn() {
        binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_selected_background)
        binding.agreeSignupBtn.isEnabled = true
    }

    private fun inActiveAgreeSignUpBtn() {
        binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_unselected_background)
        binding.agreeSignupBtn.isEnabled = false
    }
}