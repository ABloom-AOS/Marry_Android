package com.abloom.mery.data.firebase.qna

import com.abloom.domain.qna.model.Response

fun Int.asResponse(): Response = Response.entries[this]

fun Response.asReaction(): Int = ordinal
