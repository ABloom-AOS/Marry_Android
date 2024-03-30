package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentSignUpBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.view.setOnActionClick
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val signUpViewModel: SignUpViewModel by viewModels()

    private val brideGroomSelectionFragment by lazy { BrideGroomSelectionFragment() }
    private val inputNameFragment by lazy { InputNameFragment() }

    private val signUpFragmentManager by lazy { childFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initBindingViewModel()
        initBrideGroomFragment()
    }

    private fun initBrideGroomFragment() {
        signUpFragmentManager.beginTransaction().apply {
            replace(
                R.id.fragmentContainerView,
                brideGroomSelectionFragment,
            )
            addToBackStack("signfragment")
            commit()
        }

        setupForBrideGroomSelection()
    }


    private fun initBindingViewModel() {
        binding.viewModel = signUpViewModel
    }

    private fun initListener() {
        binding.appbarSignUp.setOnNavigationClick { navigateToPriorFragment() }
        binding.appbarSignUp.setOnActionClick { navigateToNextFragment() }
    }

    private fun navigateToNextFragment() {
        when (getStackCount()) {
            STEP_MARRY_DATE_SELECTION -> {
                signUpFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, inputNameFragment)
                    .addToBackStack("signfragment")
                    .commit()
            }

            STEP_INPUT_NAME_SELECTION -> {
                //TODO("STEP 04 약간 동의 화면으로 이동)
            }
        }
    }

    private fun getStackCount() = signUpFragmentManager.backStackEntryCount


    private fun navigateToPriorFragment() {
        signUpFragmentManager.popBackStackImmediate()
        printBackStack()

        when (getStackCount()) {
            STEP_BRIDE_GROOM_SELECTION -> {
                findNavController().popBackStack()
            }

            STEP_MARRY_DATE_SELECTION -> {
                setupForBrideGroomSelection()
            }

            STEP_INPUT_NAME_SELECTION -> {
                setupForMarryDate()
            }
        }

    }

    private fun setupForBrideGroomSelection() {
        binding.appbarSignUp.apply {
            title = getString(R.string.signup_title)
            navigationText = getString(R.string.all_cancel)
            actionText = ""
            isActionEnabled = false
        }
        updateProgressBarState(STEP_BRIDE_GROOM_SELECTION)
    }

    private fun setupForMarryDate() {
        binding.appbarSignUp.apply {
            title = ""
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_button)
            actionText = getString(R.string.all_next)
            isActionEnabled = true
        }
        updateProgressBarState(STEP_MARRY_DATE_SELECTION)
    }

    private fun setupForInputName() {
        binding.appbarSignUp.apply {
            title = ""
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_button)
            actionText = getString(R.string.all_next)
            isActionEnabled = false
        }
        updateProgressBarState(STEP_INPUT_NAME_SELECTION)
    }

    fun printBackStack() {
        val count = signUpFragmentManager.backStackEntryCount
        Log.e("BackStack", "There are $count entries in the back stack")
        for (i in 0 until count) {
            val entry = signUpFragmentManager.getBackStackEntryAt(i)
            Log.e("BackStack", "Entry $i: ${entry.name}")
        }
    }


    private fun updateProgressBarState(state: Int) {
        binding.signupProgressBar.progress = state
    }

    companion object {
        private const val STEP_BRIDE_GROOM_SELECTION = 1
        private const val STEP_MARRY_DATE_SELECTION = 2
        private const val STEP_INPUT_NAME_SELECTION = 3
    }
}
