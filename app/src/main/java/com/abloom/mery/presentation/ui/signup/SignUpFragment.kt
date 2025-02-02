package com.abloom.mery.presentation.ui.signup

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.MixpanelManager
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentSignUpBinding
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.common.view.setOnActionClick
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : NavigationFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    @Inject
    lateinit var mixpanelManager: MixpanelManager

    private val signUpViewModel: SignUpViewModel by viewModels()

    private val brideGroomSelectionFragment by lazy { BrideGroomSelectionFragment() }
    private val inputNameFragment by lazy { InputNameFragment() }
    private val privacyConsentFragment by lazy { PrivacyConsentFragment() }
    private val fragmentList =
        listOf(brideGroomSelectionFragment, inputNameFragment, privacyConsentFragment)

    private val signUpFragmentManager by lazy { childFragmentManager }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initBindingViewModel()
        initOnBackPressed()
        initSignUpFragmentManager()

        if (getStackCount() == 0)
            initBrideGroomFragment()
        else
            privacyConsentAppBar()
    }

    private fun initListener() {
        binding.appbarSignUp.setOnNavigationClick { navigateToPriorFragment() }
        binding.appbarSignUp.setOnActionClick {
            navigateToNextFragment()
        }
    }

    private fun initBindingViewModel() {
        binding.viewModel = signUpViewModel
    }

    private fun initBrideGroomFragment() {
        replaceBrideGroomSelectionFragment()
        changeToBrideGroomUi()
    }

    private fun initOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToPriorFragment()
                }
            })
    }

    private fun initSignUpFragmentManager() {
        signUpFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentCreated(
                    manager: FragmentManager,
                    curFragment: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    when (getStackCount()) {
                        STEP_MARRY_DATE_SELECTION -> {
                            changeToMarryDateUi()
                        }
                    }
                }
            }, true
        )
    } // BrideGroomFragment에서 Selection을 감지하기 위해 구현

    private fun navigateToNextFragment() {
        when (getStackCount()) {
            STEP_MARRY_DATE_SELECTION -> {
                mixpanelManager.setMarryDate(signUpViewModel.selectedMarriage.value)
                replaceInputNameFragment()
                changeInputNameUi()
            }

            STEP_INPUT_NAME_SELECTION -> {
                mixpanelManager.setInputName(signUpViewModel.name.value)
                replacePrivacyConsentFragment()
                changePrivacyConsentUi()
            }
        }
    }

    private fun replaceBrideGroomSelectionFragment() {
        fragmentTransaction(FRAGMENT_LIST_BRIDE_GROOM_INDEX)
    }

    private fun replaceInputNameFragment() {
        fragmentTransaction(FRAGMENT_LIST_INPUT_NAME_GROOM_INDEX)
    }

    private fun replacePrivacyConsentFragment() {
        fragmentTransaction(FRAGMENT_LIST_PRIVACY_CONSENT_INDEX)
    }

    private fun fragmentTransaction(fragmentIdx: Int) {
        signUpFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace(R.id.fragmentContainerView, fragmentList[fragmentIdx])
            addToBackStack(null)
            commit()
        }
    }

    private fun navigateToPriorFragment() {
        signUpFragmentManager.popBackStackImmediate()

        when (getStackCount()) {
            STEP_INIT_SIGN_FRAGMENT -> {
                findNavController().popBackStack()
            }

            STEP_BRIDE_GROOM_SELECTION -> {
                changeToBrideGroomUi()
            }

            STEP_MARRY_DATE_SELECTION -> {
                changeToMarryDateUi()
            }

            STEP_INPUT_NAME_SELECTION -> {
                changeInputNameUi()
            }
        }
    }

    private fun getStackCount() = signUpFragmentManager.backStackEntryCount

    private fun changeToBrideGroomUi() {
        brideGroomAppBar()
        updateProgressBarState(PROGRESS_BRIDE_GROOM_STATE)
    }

    private fun changeToMarryDateUi() {
        marryDateAppBar()
        updateProgressBarState(PROGRESS_MARRY_DATE_STATE)
    }

    private fun changeInputNameUi() {
        inputNameAppBar()
        updateProgressBarState(PROGRESS_INPUT_NAME_STATE)
    }

    private fun changePrivacyConsentUi() {
        privacyConsentAppBar()
        updateProgressBarState(PROGRESS_PRIVACY_CONSENT_STATE)
    }

    private fun brideGroomAppBar() {
        binding.appbarSignUp.apply {
            title = getString(R.string.signup_title)
            navigationText = getString(R.string.all_cancel)
            actionText = ""
            isActionEnabled = false
        }
    }

    private fun marryDateAppBar() {
        binding.appbarSignUp.apply {
            title = ""
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_button)
            actionText = getString(R.string.all_next)
            isActionEnabled = true
        }
    }

    private fun inputNameAppBar() {
        binding.appbarSignUp.apply {
            title = ""
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_button)
            actionText = getString(R.string.all_next)
            isActionEnabled = signUpViewModel.name.value.isNotEmpty()
        }
    }

    private fun privacyConsentAppBar() {
        binding.appbarSignUp.apply {
            title = getString(R.string.signup_title)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_button)
            actionText = ""
            isActionEnabled = false
        }
    }

    private fun updateProgressBarState(state: Int) {
        val progressAnimator = ObjectAnimator.ofInt(
            binding.signupProgressBar,
            "progress",
            binding.signupProgressBar.progress,
            state
        )
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.start()
    }

    companion object {

        private const val FRAGMENT_LIST_BRIDE_GROOM_INDEX = 0
        private const val FRAGMENT_LIST_INPUT_NAME_GROOM_INDEX = 1
        private const val FRAGMENT_LIST_PRIVACY_CONSENT_INDEX = 2

        private const val STEP_INIT_SIGN_FRAGMENT = 0
        private const val STEP_BRIDE_GROOM_SELECTION = 1
        private const val STEP_MARRY_DATE_SELECTION = 2
        private const val STEP_INPUT_NAME_SELECTION = 3

        private const val PROGRESS_BRIDE_GROOM_STATE = 25
        private const val PROGRESS_MARRY_DATE_STATE = 50
        private const val PROGRESS_INPUT_NAME_STATE = 75
        private const val PROGRESS_PRIVACY_CONSENT_STATE = 100
    }
}
