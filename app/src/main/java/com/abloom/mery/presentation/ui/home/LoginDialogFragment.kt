package com.abloom.mery.presentation.ui.home

import android.app.Activity.RESULT_OK
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
import com.abloom.mery.presentation.common.extension.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentLoginDialogBinding
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val googleClient: GoogleSignInClient by lazy { getGoogleSignInClient() }
    private val googleAuthLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult
        val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            .getResult(ApiException::class.java)
        val googleToken = account.idToken.toString()
        loginAndDismiss(Authentication.Google(googleToken))
    }

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .build()
        return GoogleSignIn.getClient(requireActivity(), googleSignInOption)
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

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.onKakaoButtonClick = ::handleKakaoButtonClick
        binding.onGoogleButtonClick = ::handleGoogleButtonClick
    }

    private fun handleKakaoButtonClick() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = ::handleOAuth)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = ::handleOAuth)
        }
    }

    private fun handleOAuth(token: OAuthToken?, error: Throwable?) {
        if (token != null) {
            tryToLoginWithKakao()
            return
        }
        if (error == null) return
        val stringRes = when (error.toString()) {
            AuthErrorCause.AccessDenied.toString() -> R.string.access_denied
            AuthErrorCause.InvalidClient.toString() -> R.string.invalid_error
            AuthErrorCause.InvalidGrant.toString() -> R.string.can_not_authentication
            AuthErrorCause.InvalidRequest.toString() -> R.string.request_parameter_error
            AuthErrorCause.InvalidScope.toString() -> R.string.invalid_scope_id
            AuthErrorCause.Misconfigured.toString() -> R.string.setting_not_right
            AuthErrorCause.ServerError.toString() -> R.string.server_internal_error
            AuthErrorCause.Unauthorized.toString() -> R.string.not_have_request_permission
            else -> R.string.other_error
        }
        context?.showToast(stringRes)
    }

    private fun tryToLoginWithKakao() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                context?.showToast(R.string.kakao_get_email_failed)
                return@me
            }
            val email = user?.kakaoAccount?.email.toString()
            val password = user?.id.toString()
            loginAndDismiss(Authentication.Kakao(email, password))
        }
    }

    private fun loginAndDismiss(authentication: Authentication) {
        viewModel.login(authentication)
        dismiss()
    }

    private fun handleGoogleButtonClick() {
        googleClient.signOut()
        val signInIntent = googleClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }
}
