package com.abloom.mery

import android.annotation.SuppressLint
import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject
import java.time.LocalDate

object MixpanelManager {

    @SuppressLint("StaticFieldLeak")
    private var mixpanel: MixpanelAPI? = null
    private var trackAutomaticEvents = false

    fun getInstance(context: Context): MixpanelAPI {
        if (mixpanel == null) {
            mixpanel =
                MixpanelAPI.getInstance(context, BuildConfig.MIX_PANEL_TOKEN, trackAutomaticEvents)
        }
        return mixpanel!!
    }

    fun setGoogleLogin(googleToken: String) {
        identifyUser(googleToken)
        setPeopleProperty("Social Login", "Google")
        trackEvent("signup_social", JSONObject().apply {
            put("Social Login", "Google")
        })
    }

    fun setKakaoLogin(email: String) {
        identifyUser(email)
        setPeopleProperty("Social Login", "Kakao")
        trackEvent("signup_social", JSONObject().apply {
            put("Social Login", "Kakao")
        })
    }

    private fun identifyUser(token: String) {
        mixpanel?.identify(token)
    }

    fun setGroomSelection() {
        setPeopleProperty("Sex", "예비 신랑")
        trackEvent("signup_sex_type", JSONObject().apply {
            put("Sex", "예비 신랑")
        })
    }

    fun setBrideSelection() {
        setPeopleProperty("Sex", "예비 신부")
        trackEvent("signup_sex_type", JSONObject().apply {
            put("Sex", "예비 신부")
        })
    }

    fun setMarryDate(marriageDate: LocalDate) {
        setPeopleProperty("Marriage Date", marriageDate.toString())
        trackEvent("signup_date", JSONObject().apply {
            put("Marriage Date", marriageDate.toString())
        })
    }

    fun setInputName(userName: String) {
        setPeopleProperty("name", userName)
        trackEvent("signup_name", JSONObject().apply {
            put("name", userName)
        })
    }

    fun setPrivacyConsent() {
        trackEvent("signup_complete", JSONObject())
    }

    private fun setPeopleProperty(property: String, value: String) {
        mixpanel?.people?.set(property, value)
    }

    private fun trackEvent(event: String, properties: JSONObject) {
        mixpanel?.track(event, properties)
    }
}
