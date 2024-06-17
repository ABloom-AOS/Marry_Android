package com.abloom.mery.data.firebase.qna

import androidx.annotation.Keep
import com.abloom.mery.data.firebase.Document
import com.abloom.mery.data.firebase.toTimestamp
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Keep
@Serializable
class QnaDocument(
    val user_id: String = "",
    val q_id: Long = -1,
    val date: Timestamp = Timestamp.now(),
    val answer_content: String = "",
    val reaction: Int? = null,
    val is_complete: Boolean = false,
) : Document {

    constructor(
        userId: String,
        questionId: Long,
        date: LocalDateTime,
        answer: String,
    ) : this(
        user_id = userId,
        q_id = questionId,
        date = date.toTimestamp(),
        answer_content = answer,
    )
}
