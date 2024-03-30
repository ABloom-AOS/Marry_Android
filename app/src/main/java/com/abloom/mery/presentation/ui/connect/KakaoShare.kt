package com.abloom.mery.presentation.ui.connect

import android.content.ActivityNotFoundException
import android.content.Context
import com.abloom.mery.R
import com.abloom.mery.presentation.common.util.showToast
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient

private const val TEMPLATE_ID = 103009L
private const val KEY_USER_NAME = "userName"
private const val KEY_CODE = "code"

fun Context.shareInvitationCodeToKakao(userName: String, invitationCode: String) {
    if (isKakaoInstalled) {
        tryToShareWithKakao(userName = userName, invitationCode = invitationCode)
    } else {
        tryToShareWithWebBrowser(userName = userName, invitationCode = invitationCode)
    }
}

private val Context.isKakaoInstalled: Boolean
    get() = ShareClient.instance.isKakaoTalkSharingAvailable(this)

private fun Context.tryToShareWithKakao(userName: String, invitationCode: String) {
    ShareClient.instance.shareCustom(
        context = this,
        templateId = TEMPLATE_ID,
        templateArgs = getTemplateArgs(userName = userName, invitationCode = invitationCode)
    ) { sharingResult, error ->
        if (error != null) {
            showToast(R.string.connect_kakao_share_fail_message)
        } else if (sharingResult != null) {
            startActivity(sharingResult.intent)
        }
    }
}

private fun Context.tryToShareWithWebBrowser(
    userName: String,
    invitationCode: String
) {
    val sharerUrl = WebSharerClient.instance.makeCustomUrl(
        templateId = TEMPLATE_ID,
        templateArgs = getTemplateArgs(userName = userName, invitationCode = invitationCode)
    )

    runCatching {
        KakaoCustomTabsClient.openWithDefault(this, sharerUrl)
    }.recoverCatching { error ->
        if (error is UnsupportedOperationException) KakaoCustomTabsClient.open(this, sharerUrl)
    }.onFailure { error ->
        if (error is ActivityNotFoundException) showToast(R.string.connect_not_found_browser)
    }
}

private fun getTemplateArgs(userName: String, invitationCode: String): Map<String, String> = mapOf(
    KEY_USER_NAME to userName,
    KEY_CODE to invitationCode
)
