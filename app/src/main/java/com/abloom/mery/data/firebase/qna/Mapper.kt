package com.abloom.mery.data.firebase.qna

import com.abloom.domain.qna.model.Response

fun Int.asResponse(): Response = Response.entries[this]

fun Int.isPositiveResponse(): Boolean =
    this == Response.GOOD.ordinal || this == Response.BETTER_KNOW.ordinal

fun Response.asReaction(): Int = ordinal
