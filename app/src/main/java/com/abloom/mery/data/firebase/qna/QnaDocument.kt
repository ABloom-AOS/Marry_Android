package com.abloom.mery.data.firebase.qna

import com.abloom.domain.qna.model.Response
import com.abloom.mery.data.firebase.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.time.LocalDateTime

data class QnaDocument(
    @JvmField @PropertyName(KEY_USER_ID) val userId: String = "",
    @JvmField @PropertyName(KEY_QUESTION_ID) val questionId: Long = -1,
    @JvmField @PropertyName(KEY_DATE) val date: Timestamp = Timestamp(0, 0),
    @JvmField @PropertyName(KEY_ANSWER_CONTENT) val answer: String = "",
    @JvmField @PropertyName(KEY_REACTION) val reaction: Int? = null,
) {

    companion object {

        const val KEY_USER_ID = "user_id"
        const val KEY_QUESTION_ID = "q_id"
        const val KEY_DATE = "date"
        const val KEY_ANSWER_CONTENT = "answer_content"
        const val KEY_REACTION = "reaction"

        fun create(
            userId: String,
            questionId: Long,
            date: LocalDateTime,
            answer: String,
            reaction: Response? = null,
        ) = QnaDocument(
            userId = userId,
            questionId = questionId,
            date = date.toTimestamp(),
            answer = answer,
            reaction = reaction?.ordinal,
        )
    }
}
