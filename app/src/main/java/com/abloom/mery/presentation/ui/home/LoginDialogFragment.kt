package com.abloom.mery.presentation.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.abloom.domain.user.model.Authentication
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentLoginDialogBinding
import com.abloom.mery.presentation.common.util.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private lateinit var binding: FragmentLoginDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kakaoAutoLogin()

        binding.onKakaoButtonClick = ::checkUserApiClient


        /*
        // TODO("임시로 애플 로그인 버튼 클릭하면 로그인되도록 했습니다. 지워야 합니다")
        binding.appleLoginBtn.setOnClickListener {
            viewModel.login(Authentication.Apple("asdf"))
            dismiss()
        }
        */


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
    }

    private fun kakaoAutoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null)
                context?.showToast(R.string.kakao_login_failed)
            else if (tokenInfo != null)
                kakaoLoginSuccess()
        }
    }

    private fun kakaoLoginSuccess() {
        context?.showToast(R.string.kakao_login_text)

        // TODO("화면 이동 로직 구현")
        // 파이어베이스를 조회하여 기존 회원이 아닌 경우 회원가입 화면으로 이동한다.

        dismiss()
    }

    private fun checkUserApiClient() {

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

            if (error != null) {
                when (error.toString()) {

                    AuthErrorCause.AccessDenied.toString() -> {
                        context?.showToast(R.string.access_denied)
                    }

                    AuthErrorCause.InvalidClient.toString() -> {
                        context?.showToast(R.string.invalid_error)
                    }

                    AuthErrorCause.InvalidGrant.toString() -> {
                        context?.showToast(R.string.can_not_authentication)
                    }

                    AuthErrorCause.InvalidRequest.toString() -> {
                        context?.showToast(R.string.request_parameter_error)
                    }

                    AuthErrorCause.InvalidScope.toString() -> {
                        context?.showToast(R.string.invalid_scope_id)
                    }

                    AuthErrorCause.Misconfigured.toString() -> {
                        context?.showToast(R.string.setting_not_right)
                    }

                    AuthErrorCause.ServerError.toString() -> {
                        context?.showToast(R.string.server_internal_error)
                    }

                    AuthErrorCause.Unauthorized.toString() -> {
                        context?.showToast(R.string.not_have_request_permission)
                    }

                    else -> {
                        context?.showToast(R.string.other_error)
                    } // Unknown

                }
            } else if (token != null) {
                kakaoLoginSuccess()
            }

        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext()))
            UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
        else
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
    }
}
