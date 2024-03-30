package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.abloom.domain.user.model.Sex
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentBrideGroomSelectionBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrideGroomSelectionFragment :
    BaseFragment<FragmentBrideGroomSelectionBinding>(R.layout.fragment_bride_groom_selection) {

    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })
    private val marryDateFragment by lazy { MarryDateFragment() }
    private val signUpFragmentManager by lazy { parentFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initBindingViewModel()
    }

    private fun initListener() {
        binding.groomBut.setOnClickListener {
            moveToMarryDateFragment()
            signUpViewModel.selectSex(Sex.MALE)
        }

        binding.brideBut.setOnClickListener {
            moveToMarryDateFragment()
            signUpViewModel.selectSex(Sex.FEMALE)
        }
    }

    private fun initBindingViewModel() {
        binding.viewModel = signUpViewModel
    }

    private fun moveToMarryDateFragment() {
        signUpFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, marryDateFragment)
            .addToBackStack(null)
            .commit()
    }
}
