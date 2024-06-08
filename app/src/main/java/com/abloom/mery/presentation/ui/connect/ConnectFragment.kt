package com.abloom.mery.presentation.ui.connect

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.MixpanelManager
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentConnectBinding
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.common.extension.copyToClipboard
import com.abloom.mery.presentation.common.extension.hideSoftKeyboard
import com.abloom.mery.presentation.common.extension.repeatOnStarted
import com.abloom.mery.presentation.common.extension.showToast
import com.abloom.mery.presentation.common.view.ConfirmDialog
import com.abloom.mery.presentation.common.view.InfoDialog
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConnectFragment : NavigationFragment<FragmentConnectBinding>(R.layout.fragment_connect) {

    @Inject
    lateinit var mixpanelManager: MixpanelManager

    private val viewModel: ConnectViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppBar()
        setupDataBinding()

        observeConnectEvent()
    }

    private fun setupAppBar() {
        binding.appbarConnect.setOnNavigationClick { findNavController().popBackStack() }
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onInvitationCodeCopyButtonClick = { invitationCode ->

            mixpanelManager.copyConnectCode(invitationCode)
            copyToClipboard(invitationCode)
            // 33 이상부터는 클립보드에 복사할 때 기본적으로 토스트 메세지를 보여줍니다.
            if (SDK_INT < VERSION_CODES.TIRAMISU) requireContext().showToast(R.string.connect_invitation_code_copy_success_message)
        }
        binding.onConnectButtonClick = { fianceInvitationCode ->
            view?.hideSoftKeyboard()
            viewModel.connectWithFiance(fianceInvitationCode)
        }
        binding.onKakaoShareButtonClick = ::showKakaoOpenConfirmDialog
    }

    private fun copyToClipboard(text: String) {
        requireContext().copyToClipboard(
            label = getString(R.string.connect_invitation_code_clipboard_label),
            text = text
        )
    }

    private fun showKakaoOpenConfirmDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.connect_kakao_open_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.all_open),
            onPositiveButtonClick = ::shareInvitationCodeToKakao
        ).show()
    }

    private fun shareInvitationCodeToKakao() {
        val loginUser = viewModel.loginUser.value ?: return
        requireContext().shareInvitationCodeToKakao(
            userName = loginUser.name,
            invitationCode = loginUser.invitationCode
        )
        mixpanelManager.shareKakao(loginUser.invitationCode)
    }

    private fun observeConnectEvent() {
        repeatOnStarted { viewModel.event.collect(::handleEvent) }
    }

    private fun handleEvent(event: ConnectEvent) {
        when (event) {
            ConnectEvent.ConnectFail -> showConnectFailDialog()
        }
    }

    private fun showConnectFailDialog() {
        InfoDialog(
            context = requireContext(),
            title = getString(R.string.connect_connect_fail_dialog_title),
            message = getString(R.string.connect_connect_fail_dialog_message),
        ).show()
    }
}
