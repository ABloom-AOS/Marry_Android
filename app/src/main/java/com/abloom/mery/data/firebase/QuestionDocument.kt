package com.abloom.mery.data.firebase

import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.google.firebase.firestore.PropertyName

data class QuestionDocument(
    @JvmField @PropertyName(KEY_QUESTION_ID) val id: Long = -1,
    @JvmField @PropertyName(KEY_CATEGORY) val category: String = "",
    @JvmField @PropertyName(KEY_CONTENT) val content: String = ""
) {

    fun asExternal() = Question(
        id = id,
        category = category.asCategory(),
        content = content
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

    companion object {

        const val KEY_QUESTION_ID = "q_id"
        const val KEY_CATEGORY = "category"
        const val KEY_CONTENT = "content"
    }
}

fun List<QuestionDocument>.asExternal(): List<Question> = map(QuestionDocument::asExternal)
