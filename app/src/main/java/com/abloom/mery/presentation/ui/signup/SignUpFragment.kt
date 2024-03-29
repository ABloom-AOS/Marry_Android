package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
    private val marryDateFragment by lazy { MarryDateFragment() }
    private val inputNameFragment by lazy { InputNameFragment() }

    private val signUpFragmentManager by lazy { childFragmentManager }
    private var curFragmentName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initBindingViewModel()
        initBrideGroomFragment()
        observeSignUpFragmentManager()
    }

    private fun initBrideGroomFragment() {
        signUpFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, brideGroomSelectionFragment)
            .commitNow()

        setupForBrideGroomSelection()
    }

    private fun observeSignUpFragmentManager() {
        signUpFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentCreated(
                    manager: FragmentManager,
                    curFragment: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    when (curFragment.javaClass.simpleName) {
                        brideGroomSelectionFragment.javaClass.simpleName -> {
                            setupForBrideGroomSelection()
                        }

                        marryDateFragment.javaClass.simpleName -> {
                            setupForMarryDate()
                        }

                        inputNameFragment.javaClass.simpleName -> {
                            setupForInputName()
                        }
                    }
                }
            }, true
        )
    }

    private fun initBindingViewModel() {
        binding.viewModel = signUpViewModel
    }

    private fun initListener() {
        binding.appbarSignUp.setOnNavigationClick { navigateToPriorFragment() }
        binding.appbarSignUp.setOnActionClick { navigateToNextFragment() }
    }

    private fun navigateToNextFragment() {
        getCurFragmentName()

        when (curFragmentName) {
            marryDateFragment.javaClass.simpleName -> {
                signUpFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, inputNameFragment)
                    .commitNow()
            }

            inputNameFragment.javaClass.simpleName -> {
                //TODO("STEP 04 약간 동의 화면으로 이동)
            }
        }
    }

    private fun getCurFragmentName() {
        curFragmentName =
            signUpFragmentManager.findFragmentById(R.id.fragmentContainerView)!!::class.simpleName.toString()
    }

    private fun navigateToPriorFragment() {
        getCurFragmentName()

        when (curFragmentName) {
            brideGroomSelectionFragment.javaClass.simpleName -> {
                findNavController().popBackStack()
            }

            marryDateFragment.javaClass.simpleName -> {
                signUpFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, brideGroomSelectionFragment)
                    .commitNow()
            }

            inputNameFragment.javaClass.simpleName -> {
                signUpFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, marryDateFragment)
                    .commitNow()
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

    private fun updateProgressBarState(state: Int) {
        binding.signupProgressBar.progress = state
    }

    companion object {
        private const val STEP_BRIDE_GROOM_SELECTION = 1
        private const val STEP_MARRY_DATE_SELECTION = 2
        private const val STEP_INPUT_NAME_SELECTION = 3
    }
}
