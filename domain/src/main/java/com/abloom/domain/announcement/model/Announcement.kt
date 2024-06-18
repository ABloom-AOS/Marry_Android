package com.abloom.domain.announcement.model

import java.time.LocalDateTime

data class Announcement(
    val title: String,
    val url: String,
    val createdAt: LocalDateTime,
)
