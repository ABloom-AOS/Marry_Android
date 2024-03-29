package com.abloom.mery.presentation.ui.connect

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentConnectBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.copyToClipboard
import com.abloom.mery.presentation.common.util.hideSoftKeyboard
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.common.util.showToast
import com.abloom.mery.presentation.common.view.InfoDialog
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectFragment : BaseFragment<FragmentConnectBinding>(R.layout.fragment_connect) {

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
            copyToClipboard(invitationCode)
            requireContext().showToast(R.string.connect_invitation_code_copy_success_message)
        }
        binding.onConnectButtonClick = { fianceInvitationCode ->
            view?.hideSoftKeyboard()
            viewModel.connectWithFiance(fianceInvitationCode)
        }
    }

    private fun copyToClipboard(text: String) {
        requireContext().copyToClipboard(
            label = getString(R.string.connect_invitation_code_clipboard_label),
            text = text
        )
    }

    private fun observeConnectEvent() {
        repeatOnStarted { viewModel.event.collect(::handleEvent) }
    }

    private fun handleEvent(event: ConnectEvent) {
        when (event) {
            ConnectEvent.ConnectFail -> {
                InfoDialog(
                    context = requireContext(),
                    title = "연결에 실패했어요",
                    message = "상대방의 연결 코드를 올바르게\n입력했는지 확인해주세요.",
                ).show()
            }
        }
    }
}
