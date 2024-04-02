package com.abloom.mery.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.abloom.domain.user.model.Authentication
import com.abloom.mery.BuildConfig
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentLoginDialogBinding
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.common.util.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient


class LoginDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLoginDialogBinding
    private val homeViewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private val googleAuthLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .getResult(ApiException::class.java)
            val googleToken = account.idToken.toString()
            homeViewModel.login(Authentication.Google(googleToken))
        }

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
        initBinding()
        observeLoginFail()
    }


    private fun initBinding() {
        binding.onKakaoButtonClick = ::checkUserKakaoApiClient
        binding.onGoogleButtonClick = ::requestGoogleLogin
    }

    private fun observeLoginFail() {
        repeatOnStarted {
            homeViewModel.event.collect { homeEvent ->
                when (homeEvent) {
                    is HomeEvent.LoginFail -> {
                        //TODO("가입하기 정보화면으로 이동")
                    }
                }
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

    /* 카카오 로그인 관련 코드 */
    private fun checkUserKakaoApiClient() {
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
            } else if (token != null)
                getKakaoEmail()
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext()))
            UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
        else
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
    }

    private fun kakaoAutoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                context?.showToast(R.string.kakao_login_failed)
            } else if (tokenInfo != null) {
                getKakaoEmail()
            }
        }
    }

    private fun getKakaoEmail() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                context?.showToast(R.string.kakao_get_email_failed)
            } else {
                val kakaoUserEmail = user?.kakaoAccount?.email.toString()
                val kakaoPassword = user?.id.toString()
                kakaoLoginSuccess(kakaoUserEmail, kakaoPassword)
            }
        }
    }

    private fun kakaoLoginSuccess(kakaoUserEmail: String, kakaoPassword: String) {
        context?.showToast(R.string.kakao_login_text)
        homeViewModel.login(Authentication.Kakao(kakaoUserEmail, kakaoPassword))
    }
    /* 애플 로그인 관련 코드 */

    /* 구글 로그인 관련 코드 */
    private fun requestGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .build()
        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
    }
}
