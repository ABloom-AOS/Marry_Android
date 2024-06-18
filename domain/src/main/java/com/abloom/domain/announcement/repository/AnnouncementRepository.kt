package com.abloom.domain.announcement.repository

import com.abloom.domain.announcement.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {

    fun getLatestAnnouncement(): Flow<Announcement?>
}
