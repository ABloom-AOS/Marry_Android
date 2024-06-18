package com.abloom.mery.data.firebase.question

import com.abloom.mery.data.firebase.Document
import kotlinx.serialization.Serializable

@Serializable
class EssentialQuestionsDocument(
    val essentials: List<Long> = emptyList(),
) : Document
