package com.abloom.mery.presentation.ui.profilemenu

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.domain.user.model.MarriageState
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentProfileMenuBinding
import com.abloom.mery.presentation.MainViewModel
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.extension.repeatOnStarted
import com.abloom.mery.presentation.common.view.ConfirmDialog
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import com.abloom.mery.presentation.ui.profilemenu.dialog.ProfileDetailMenuDialog
import com.abloom.mery.presentation.ui.webview.WebViewUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileMenuFragment :
    BaseFragment<FragmentProfileMenuBinding>(R.layout.fragment_profile_menu) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val profileMenuViewModel: ProfileMenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppBar()
        setupDataBinding()

        observeLoginUser()
        observeLoginUserDescriptionUi()
    }

    private fun setupAppBar() {
        binding.appbarProfileMenu.setOnNavigationClick { findNavController().popBackStack() }
    }

    private fun setupDataBinding() {
        binding.viewModel = profileMenuViewModel
        binding.onProfileUpdateButtonClick = ::handleProfileUpdateButtonClick
        binding.onConnectSettingButtonClick = ::handleConnectSettingButtonClick
        binding.onNavigateToWebViewButtonClick = ::navigateToWebView
        binding.onLogoutButtonClick = ::showLogoutConfirmDialog
        binding.onLeaveButtonClick = ::navigateToLeave
    }

    private fun handleProfileUpdateButtonClick() {
        val isLogin = profileMenuViewModel.loginUser.value != null
        if (isLogin) showProfileDetailMenuDialog() else showLoginConfirmDialog()
    }

    private fun showProfileDetailMenuDialog() {
        ProfileDetailMenuDialog().show(childFragmentManager, null)
    }

    private fun showLoginConfirmDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.profilemenu_login_confirm_dialog_title),
            positiveButtonLabel = getString(R.string.login_text),
            onPositiveButtonClick = {
                mainViewModel.dispatchLoginEvent()
                findNavController().popBackStack(R.id.homeFragment, false)
            },
            negativeButtonLabel = getString(R.string.all_cancel),
        ).show()
    }

    private fun handleConnectSettingButtonClick() {
        val isLogin = profileMenuViewModel.loginUser.value != null
        if (isLogin) navigateToConnect() else showLoginConfirmDialog()
    }

    private fun navigateToConnect() {
        findNavController().navigate(ProfileMenuFragmentDirections.actionProfileMenuFragmentToConnectFragment())
    }

    private fun navigateToWebView(url: WebViewUrl) {
        findNavController().navigate(
            ProfileMenuFragmentDirections
                .actionProfileMenuFragmentToWebViewFromProfileMenuFragment(url)
        )
    }

    private fun navigateToLeave() {
        findNavController().navigate(ProfileMenuFragmentDirections.actionProfileMenuFragmentToLeaveFragment())
    }

    private fun showLogoutConfirmDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.profilemenu_logout_confirm_dialog_title),
            message = getString(R.string.profilemenu_logout_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.profilemenu_logout_confirm_dialog_positive_button_label),
            onPositiveButtonClick = {
                profileMenuViewModel.logout()
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        ).show()
    }

    private fun observeLoginUser() {
        repeatOnStarted { profileMenuViewModel.loginUser.collect(::handleLoginUser) }
    }

    private fun handleLoginUser(user: User?) {
        updateUserImage(user)
        updateMarriageDate(user)
    }

    private fun updateUserImage(user: User?) {
        binding.ivProfilemenuUserImage.setImageDrawable(
            when {
                user == null -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_profilemenu_not_login_user
                )

                user.sex == Sex.MALE -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_all_groom
                )

                user.sex == Sex.FEMALE -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_all_groom
                )

                else -> throw AssertionError("여기까지 올리가 없음.")
            }
        )
    }

    private fun updateMarriageDate(user: User?) {
        binding.tvProfilemenuMarriageDate.text = if (user == null) {
            getString(R.string.profilemenu_request_login_for_marriage_date)
        } else {
            when (val marriageState = user.marriageState) {
                is MarriageState.BeforeMarriage -> getString(
                    R.string.profilemenu_until_marriage_date_format,
                    marriageState.daysUntilMarriage
                )

                MarriageState.WeddingDay -> getString(R.string.profilemenu_congratulate_for_wedding)
                is MarriageState.AfterMarriage -> getString(
                    R.string.profilemenu_since_marriage_date_format,
                    marriageState.daysSinceMarriage
                )
            }
        }
    }

    private fun observeLoginUserDescriptionUi() {
        repeatOnStarted { profileMenuViewModel.loginUserDescriptionUiState.collect(::updateLoginUserDescriptionUi) }
    }

    private fun updateLoginUserDescriptionUi(description: LoginUserDescriptionUiState) {
        binding.tvProfilemenuLoginUserDescription.text = when (description) {
            LoginUserDescriptionUiState.NotLogin -> getString(R.string.profilemenu_press_and_login_button)
            LoginUserDescriptionUiState.NotConnected -> getString(R.string.profilemenu_press_and_connect)
            is LoginUserDescriptionUiState.Fiance -> {
                if (description.sex == Sex.MALE) {
                    getString(R.string.profilemenu_groom_name_format, description.name)
                } else {
                    getString(R.string.profilemenu_bride_name_format, description.name)
                }
            }
        }
        binding.tvProfilemenuLoginUserDescription.setOnClickListener {
            when (description) {
                LoginUserDescriptionUiState.NotLogin -> {
                    mainViewModel.dispatchLoginEvent()
                    findNavController().popBackStack(R.id.homeFragment, false)
                }

                LoginUserDescriptionUiState.NotConnected ->
                    findNavController().navigate(ProfileMenuFragmentDirections.actionProfileMenuFragmentToConnectFragment())

                is LoginUserDescriptionUiState.Fiance -> {}
            }
        }
    }
}
