package com.abloom.mery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.abloom.mery.databinding.FragmentLoginDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginDialogFragment : BottomSheetDialogFragment() {

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
        setupKakaoLoginButton()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        init(context)
    }

    private fun setupKakaoLoginButton() {

        binding.kakaoLoginBtn.setOnClickListener {
            checkUserApiClient()
        }
    }

    private fun kakaoAutoLogin() {

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null)
                showToastMessage(KAKAO_LOGIN_FAILED)
            else if (tokenInfo != null)
                kakaoLoginSuccess()
        }
    }

    private fun kakaoLoginSuccess() {
        showToastMessage(KAKAO_LOGIN_SUCCESS)

        // TODO("화면 이동 로직 구현")
        // 파이어베이스를 조회하여 기존 회원이 아닌 경우 회원가입 화면으로 이동한다.

        dismiss()
    }

    private fun checkUserApiClient() {

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->

            if (error != null) {
                when (error.toString()) {

                    AuthErrorCause.AccessDenied.toString() -> {
                        showToastMessage(ACCESS_DENIED)
                    }

                    AuthErrorCause.InvalidClient.toString() -> {
                        showToastMessage(INVALID_ERROR)
                    }

                    AuthErrorCause.InvalidGrant.toString() -> {
                        showToastMessage(CAN_NOT_AUTHENTICATION)
                    }

                    AuthErrorCause.InvalidRequest.toString() -> {
                        showToastMessage(REQUEST_PARAMETER_ERROR)
                    }

                    AuthErrorCause.InvalidScope.toString() -> {
                        showToastMessage(INVALID_SCOPE_ID)
                    }

                    AuthErrorCause.Misconfigured.toString() -> {
                        showToastMessage(SETTING_NOT_RIGHT)
                    }

                    AuthErrorCause.ServerError.toString() -> {
                        showToastMessage(SERVER_INTERNAL_ERROR)
                    }

                    AuthErrorCause.Unauthorized.toString() -> {
                        showToastMessage(NOT_HAVE_REQUEST_PERMISSION)
                    }

                    else -> {
                        showToastMessage(OTHER_ERROR)
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

    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context
        }

        private val KAKAO_LOGIN_FAILED by lazy { context.getString(R.string.kakao_login_failed) }
        private val KAKAO_LOGIN_SUCCESS by lazy { context.getString(R.string.kakao_login_text) }
        private val OTHER_ERROR by lazy { context.getString(R.string.other_error) }
        private val INVALID_ERROR by lazy { context.getString(R.string.invalid_error) }
        private val ACCESS_DENIED by lazy { context.getString(R.string.access_denied) }
        private val REQUEST_PARAMETER_ERROR by lazy { context.getString(R.string.request_parameter_error) }
        private val CAN_NOT_AUTHENTICATION by lazy { context.getString(R.string.can_not_authentication) }
        private val INVALID_SCOPE_ID by lazy { context.getString(R.string.invalid_scope_id) }
        private val SETTING_NOT_RIGHT by lazy { context.getString(R.string.setting_not_right) }
        private val SERVER_INTERNAL_ERROR by lazy { context.getString(R.string.server_internal_error) }
        private val NOT_HAVE_REQUEST_PERMISSION by lazy { context.getString(R.string.not_have_request_permission) }
    }
}
