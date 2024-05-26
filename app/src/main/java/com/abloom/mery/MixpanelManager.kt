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
            mixpanel = MixpanelAPI.getInstance(context, BuildConfig.MIX_PANEL_TOKEN,trackAutomaticEvents)
        }
        return mixpanel!!
    }

    fun setGoogleLoginMixPanel(googleToken : String){
        val props = JSONObject()
        props.put("Social Login", "Google")
        mixpanel?.identify(googleToken, true);
        mixpanel?.people?.set("Social Login", "Google");
        mixpanel?.track("signup_social", props)
    }

    fun setKakaoLoginMixPanel(email : String){
        val props = JSONObject()
        props.put("Social Login", "Kakao")
        mixpanel?.identify(email, true);
        mixpanel?.people?.set("Social Login", "Kakao")
        mixpanel?.track("signup_social", props)
    }


    fun setGroomSelectionMixPanel(){
        val props = JSONObject()
        props.put("Sex", "예비 신랑")
        mixpanel?.people?.set("Sex", "예비 신랑")
        mixpanel?.track("signup_sex_type", props)
    }

    fun setBrideSelectionMixPanel(){
        val props = JSONObject()
        props.put("Sex", "예비 신부")
        mixpanel?.people?.set("Sex", "예비 신부")
        mixpanel?.track("signup_sex_type", props)
    }

    fun setMarryDateMixPanel(marriageDate : LocalDate){
        val props = JSONObject()
        props.put("Marriage Date", marriageDate)
        mixpanel?.people?.set("Marriage Date", marriageDate)
        mixpanel?.track("signup_date", props)
    }

    fun setInputNameMixPanel(userName : String){
        val props = JSONObject()
        val name = "name"
        props.put("$name", userName)
        mixpanel?.people?.set("$name", userName);
        mixpanel?.track("signup_name", props)
    }

    fun setPrivacyConsentMixPanel(){
        mixpanel?.track("signup_complete")
    }


}