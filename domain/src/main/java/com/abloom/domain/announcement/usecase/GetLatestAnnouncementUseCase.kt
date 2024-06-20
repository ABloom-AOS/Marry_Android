package com.abloom.domain.announcement.usecase

import com.abloom.domain.announcement.model.Announcement
import com.abloom.domain.announcement.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestAnnouncementUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
) {

    operator fun invoke(): Flow<Announcement?> = announcementRepository.getLatestAnnouncement()
}
