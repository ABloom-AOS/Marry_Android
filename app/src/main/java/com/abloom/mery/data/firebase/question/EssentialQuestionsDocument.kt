package com.abloom.mery.data.firebase.question

import androidx.annotation.Keep
import com.abloom.mery.data.firebase.Document
import kotlinx.serialization.Serializable

@Keep
@Serializable
class EssentialQuestionsDocument(
    val essentials: List<Long> = emptyList(),
) : Document
