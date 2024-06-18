package com.abloom.mery.data.firebase.announcements

import com.abloom.domain.announcement.model.Announcement
import com.abloom.mery.data.firebase.Document
import com.abloom.mery.data.firebase.toLocalDateTime
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDocument(
    val title: String = "",
    val url: String = "",
    val createdAt: Timestamp = Timestamp.now(),
) : Document {

    fun asExternal() = Announcement(
        title = title,
        url = url,
        createdAt = createdAt.toLocalDateTime()
    )
}
