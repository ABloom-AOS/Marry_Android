package com.abloom.mery.presentation.ui.connect

import android.content.ActivityNotFoundException
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
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
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
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
            // 33 이상부터는 클립보드에 복사할 때 기본적으로 토스트 메세지를 보여줍니다.
            if (SDK_INT < VERSION_CODES.TIRAMISU) requireContext().showToast(R.string.connect_invitation_code_copy_success_message)
        }
        binding.onConnectButtonClick = { fianceInvitationCode ->
            view?.hideSoftKeyboard()
            viewModel.connectWithFiance(fianceInvitationCode)
        }
        binding.onKakaoShareButtonClick = ::shareKakao
    }

    private fun shareKakao() {
        val loginUser = viewModel.loginUser.value ?: return
        val templateId = 103009L
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            ShareClient.instance.shareCustom(
                context = requireContext(),
                templateId = templateId,
                templateArgs = mapOf(
                    "userName" to loginUser.name,
                    "code" to loginUser.invitationCode
                )
            ) { sharingResult, error ->
                if (error != null) {
                    requireContext().showToast("카카오톡 공유에 실패했어요.")
                } else if (sharingResult != null) {
                    startActivity(sharingResult.intent)

                    requireContext().showToast("Warning Msg: ${sharingResult.warningMsg}")
                    requireContext().showToast("Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            val sharerUrl = WebSharerClient.instance.makeCustomUrl(templateId)

            try {
                KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(requireContext(), sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
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
            ConnectEvent.ConnectFail -> showConnectFailDialog()
        }
    }

    private fun showConnectFailDialog() {
        InfoDialog(
            context = requireContext(),
            title = "연결에 실패했어요",
            message = "상대방의 연결 코드를 올바르게\n입력했는지 확인해주세요.",
        ).show()
    }
}
