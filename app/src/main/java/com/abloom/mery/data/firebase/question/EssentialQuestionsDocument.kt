package com.abloom.mery.data.firebase.question

import com.abloom.mery.data.firebase.Document
import com.google.errorprone.annotations.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class EssentialQuestionsDocument(
    val essentials: List<Long> = emptyList(),
) : Document
