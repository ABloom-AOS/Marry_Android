package com.abloom.mery

import android.util.Log
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject
import java.time.LocalDate
import javax.inject.Inject

class MixpanelManager @Inject constructor(private val mixpanelApi: MixpanelAPI) {

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
        mixpanelApi.identify(token)
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

    fun connectComplete(fianceCode: String) {
        setPeopleProperty("Fiance", fianceCode)
        trackEvent("connect_complete", JSONObject().apply {
            put("Fiance", fianceCode)
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

    fun writeAnswer(qid: Long, letterCount: Int) {
        mixpanelApi.people.increment("Answered Question", 1.0)
        trackEvent("qna_answer", JSONObject().apply {
            put("Category", mapQidToCategory(qid))
            put("Question ID", qid)
            put("Letter Count", letterCount)
        })
    }

    fun recordReaction(qid: Long, reactionType: String) {
        trackEvent("qna_reaction", JSONObject().apply {
            put("Category", mapQidToCategory(qid))
            put("Question ID", qid)
            put("Reaction Type", mapReactionTypeToKorean(reactionType))
        })
    }

    private fun mapReactionTypeToKorean(reactionType: String) =
        when (reactionType) {
            "GOOD" -> "좋아요"
            "BETTER_KNOW" -> "더 알게 됐어요"
            "LETS_TALK" -> "더 대화해봐요"
            "LETS_FIND" -> "더 알아봐요"
            else -> ""
        }

    fun recommendTodayQuestion(qid: Long) {
        trackEvent("qna_recommended_question", JSONObject().apply {
            put("Category", mapQidToCategory(qid))
            put("Question ID", qid)
        })
    }

    private fun mapQidToCategory(qid: Long): String {
        return when (qid) {
            in 1..10 -> "communication"
            in 11..30 -> "values"
            in 31..55 -> "finance"
            in 56..80 -> "lifestyle"
            in 81..100 -> "child"
            in 101..120 -> "family"
            in 121..135 -> "sex"
            in 136..145 -> "health"
            in 146..160 -> "wedding"
            in 161..175 -> "future"
            in 176..185 -> "present"
            in 186..200 -> "past"
            else -> ""
        }
    }

    private fun setPeopleProperty(property: String, value: String) {
        mixpanelApi.people.set(property, value)
    }

    private fun trackEvent(event: String, properties: JSONObject) {
        mixpanelApi.track(event, properties)
    }
}
