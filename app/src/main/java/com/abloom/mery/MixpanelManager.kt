package com.abloom.mery

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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

    fun copyConnectCode(invitationCode: String) {
        setPeopleProperty("Invitation Code", invitationCode)
        trackEvent("connect_copy", JSONObject().apply {
            put("Invitation Code", invitationCode)
        })
    }

    fun shareKakao(invitationCode: String) {
        setPeopleProperty("Invitation Code", invitationCode)
        trackEvent("connect_kakao", JSONObject().apply {
            put("Invitation Code", invitationCode)
        })
    }

    fun connectComplete(fiance: String) {
        setPeopleProperty("Fiance", fiance)
        trackEvent("connect_complete", JSONObject().apply {
            put("Fiance", fiance)
        })
    }

    fun generateQna() {
        trackEvent("qna_generate", JSONObject())
    }

    fun selectCategory(category: String) {
        Log.e("TAG", category)
        trackEvent("qna_category", JSONObject().apply {
            put("Category", category)
        })
    }

    fun selectQuestion(category: String, questionId: Long) {
        trackEvent("qna_select_question", JSONObject().apply {
            put("Category", category)
            put("Question ID", questionId)
        })
    }

    fun writeAnswer(questionId: Long, letterCount: Int) {
        mixpanel?.people?.increment("Answered Question", 1.0)
        trackEvent("qna_answer", JSONObject().apply {
            put("Category", "FINANCE")
            // TODO("Category 처리")
            put("Question ID", questionId)
            put("Letter Count", letterCount)
        })
    }

    fun recordReaction(questionId: Long, reactionType: String) {
        trackEvent("qna_reaction", JSONObject().apply {
            put("Category", "FINANCE")
            // TODO("Category 처리")
            put("Question ID", questionId)
            put("Reaction Type", mapReactionTypeToKorean(reactionType))
        })
    }

    fun recommendTodayQuestion(questionId: Long) {
        trackEvent("qna_recommended_question", JSONObject().apply {
            put("Category", "FINANCE")
            // TODO("Category 처리")
            put("Question ID", questionId)
        })
    }

    private fun setPeopleProperty(property: String, value: String) {
        mixpanel?.people?.set(property, value)
    }

    private fun trackEvent(event: String, properties: JSONObject) {
        mixpanel?.track(event, properties)
    }

    private fun mapReactionTypeToKorean(reactionType: String) =
        when (reactionType) {
            "GOOD" -> "좋아요"
            "BETTER_KNOW" -> "더 알게 됐어요"
            "LETS_TALK" -> "더 대화해봐요"
            "LETS_FIND" -> "더 알아봐요"
            else -> ""
        }
}
