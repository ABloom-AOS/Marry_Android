package com.abloom.mery.data.repository

import com.abloom.domain.announcement.model.Announcement
import com.abloom.domain.announcement.repository.AnnouncementRepository
import com.abloom.mery.data.firebase.announcements.AnnouncementDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultAnnouncementRepository @Inject constructor(
    private val announcementDataSource: AnnouncementDataSource,
) : AnnouncementRepository {

    override fun getLatestAnnouncement(): Flow<Announcement?> =
        announcementDataSource.getLatestAnnouncementFlow()
            .map { it?.asExternal() }
}
