package com.abloom.mery.presentation.ui.category

import com.abloom.domain.question.model.Category

enum class CategoryArgs {

    FINANCE,
    COMMUNICATION,
    VALUES,
    LIFESTYLE,
    CHILD,
    FAMILY,
    SEX,
    HEALTH,
    WEDDING,
    FUTURE,
    PRESENT,
    PAST;

    fun asDomain(): Category = when (this) {
        FINANCE -> Category.FINANCE
        COMMUNICATION -> Category.COMMUNICATION
        VALUES -> Category.VALUES
        LIFESTYLE -> Category.LIFESTYLE
        CHILD -> Category.CHILD
        FAMILY -> Category.FAMILY
        SEX -> Category.SEX
        HEALTH -> Category.HEALTH
        WEDDING -> Category.WEDDING
        FUTURE -> Category.FUTURE
        PRESENT -> Category.PRESENT
        PAST -> Category.PAST
    }
}
