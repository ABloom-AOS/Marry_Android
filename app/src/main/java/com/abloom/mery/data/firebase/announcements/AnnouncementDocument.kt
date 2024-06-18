package com.abloom.mery.data.firebase.announcements

import com.abloom.domain.announcement.model.Announcement
import com.abloom.mery.data.firebase.Document
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDocument(
    val title: String = "",
    val url: String = "",
) : Document {

    fun asExternal() = Announcement(
        title = title,
        url = url,
    )
}
