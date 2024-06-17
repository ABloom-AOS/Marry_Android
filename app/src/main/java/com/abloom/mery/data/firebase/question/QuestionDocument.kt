package com.abloom.mery.data.firebase.question

import androidx.annotation.Keep
import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.mery.data.firebase.Document
import kotlinx.serialization.Serializable

@Keep
@Serializable
class QuestionDocument(
    val q_id: Long = -1,
    val category: String = "",
    val content: String = "",
) : Document {

    fun asExternal() = Question(
        id = q_id,
        category = category.asCategory(),
        content = content,
    )

    private fun String.asCategory(): Category =
        when (this) {
            "finance" -> Category.FINANCE
            "communication" -> Category.COMMUNICATION
            "values" -> Category.VALUES
            "lifestyle" -> Category.LIFESTYLE
            "child" -> Category.CHILD
            "family" -> Category.FAMILY
            "sex" -> Category.SEX
            "health" -> Category.HEALTH
            "wedding" -> Category.WEDDING
            "future" -> Category.FUTURE
            "present" -> Category.PRESENT
            "past" -> Category.PAST
            else -> throw IllegalArgumentException("정의되지 않은 카테고리가 있습니다.")
        }
}

fun List<QuestionDocument>.asExternal() = map { it.asExternal() }
