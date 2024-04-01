package com.abloom.mery.presentation.ui.webview

import androidx.annotation.StringRes
import com.abloom.mery.R

enum class WebViewUrl(val url: String, @StringRes val titleId: Int) {
    TERMS_OF_USE(
        "https://jaeseoklee.notion.site/9a4458152dd045ca82f8010fb46c5776",
        R.string.webview_terms_of_use
    ),
    PRIVACY_POLICY(
        "https://jaeseoklee.notion.site/78953ca6310b40209cb993312bbf9339",
        R.string.webview_privacy_policy
    ),
    SENSITIVE_PRIVACY(
        "https://jaeseoklee.notion.site/d2acdd4992e44d969f8b2553424c9e54",
        R.string.webview_sensitive_privacy
    ),
    QUESTION_FACTORY("/questionFactory", R.string.webview_question_factory),
    CS_CENTER("/csCenter", R.string.webview_cs_center);
}
