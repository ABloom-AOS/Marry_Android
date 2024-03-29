package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.abloom.domain.user.model.Sex
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentBrideGroomSelectionBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrideGroomSelectionFragment :
    BaseFragment<FragmentBrideGroomSelectionBinding>(R.layout.fragment_bride_groom_selection) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })
    private val marryDateFragment by lazy { MarryDateFragment() }
    private val signUpFragmentManager by lazy { parentFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initBrideAndGroomSelection()
    }

    private fun initBrideAndGroomSelection() {

        repeatOnStarted {
            viewModel.selectedSex.collect {
                when (it) {
                    Sex.MALE -> {
                        binding.groomBut.setBackgroundResource(R.drawable.signup_gender_selected)
                        binding.brideBut.setBackgroundResource(R.drawable.signup_gender_unselected)
                    }

                    Sex.FEMALE -> {
                        binding.groomBut.setBackgroundResource(R.drawable.signup_gender_unselected)
                        binding.brideBut.setBackgroundResource(R.drawable.signup_gender_selected)
                    }

                    null -> {
                        binding.groomBut.setBackgroundResource(R.drawable.signup_gender_unselected)
                        binding.brideBut.setBackgroundResource(R.drawable.signup_gender_unselected)
                    }
                }

            }
        }
    }

    private fun initListener() {
        binding.groomBut.setOnClickListener {
            viewModel.selectSex(Sex.MALE)
            moveToMarryDateFragment()
        }

        binding.brideBut.setOnClickListener {
            viewModel.selectSex(Sex.FEMALE)
            moveToMarryDateFragment()
        }
    }

    private fun moveToMarryDateFragment() {
        signUpFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, marryDateFragment,"marryDateFragment")
            .commit()
    }
}
