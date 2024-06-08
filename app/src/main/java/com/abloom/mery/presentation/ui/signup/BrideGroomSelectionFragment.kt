package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.abloom.domain.user.model.Sex
import com.abloom.mery.MixpanelManager
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentBrideGroomSelectionBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BrideGroomSelectionFragment :
    BaseFragment<FragmentBrideGroomSelectionBinding>(R.layout.fragment_bride_groom_selection) {

    @Inject
    lateinit var mixpanelManager: MixpanelManager

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
            mixpanelManager.setGroomSelection()
        }

        binding.brideBut.setOnClickListener {
            moveToMarryDateFragment()
            signUpViewModel.selectSex(Sex.FEMALE)
            mixpanelManager.setBrideSelection()
        }
    }

    private fun initBindingViewModel() {
        binding.viewModel = signUpViewModel
    }

    private fun moveToMarryDateFragment() {
        signUpFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace(R.id.fragmentContainerView, marryDateFragment)
            addToBackStack(null)
            commit()
        }
    }
}
