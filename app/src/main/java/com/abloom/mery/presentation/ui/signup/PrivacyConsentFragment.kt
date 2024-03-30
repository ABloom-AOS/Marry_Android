package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
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

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->

            binding.checkBox1.isChecked = isChecked
            binding.checkBox3.isChecked = isChecked
            binding.checkBox4.isChecked = isChecked
            binding.checkBox5.isChecked = isChecked

            binding.agreeSignupBtn.isEnabled = isChecked

            if (isChecked)
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_selected_background)
            else
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_unselected_background)
        }

        binding.checkBox1.setOnCheckedChangeListener { _, isChecked ->

            binding.checkBox.isChecked = isChecked

            binding.agreeSignupBtn.isEnabled = isChecked
            if (isChecked)
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_selected_background)
            else
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_unselected_background)

        }

        binding.checkBox3.setOnCheckedChangeListener { _, isChecked ->

            binding.checkBox.isChecked = isChecked

            binding.agreeSignupBtn.isEnabled = isChecked
            if (isChecked)
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_selected_background)
            else
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_unselected_background)

        }

        binding.checkBox4.setOnCheckedChangeListener { _, isChecked ->

            binding.checkBox.isChecked = isChecked

            binding.agreeSignupBtn.isEnabled = isChecked
            if (isChecked)
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_selected_background)
            else
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_unselected_background)

        }

        binding.checkBox5.setOnCheckedChangeListener { _, isChecked ->

            binding.checkBox.isChecked = isChecked

            binding.agreeSignupBtn.isEnabled = isChecked

            if (isChecked)
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_selected_background)
            else
                binding.agreeSignupBtn.setBackgroundResource(R.drawable.agree_signup_btn_unselected_background)
        }

        binding.agreeSignupBtn.setOnClickListener {
            //viewModel.join()
            findNavController().popBackStack()
        }

    }
}