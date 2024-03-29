package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentInputNameBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputNameFragment : BaseFragment<FragmentInputNameBinding>(R.layout.fragment_input_name) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindingViewModel()
    }

    private fun initBindingViewModel() {
        binding.viewModel = viewModel
    }
}