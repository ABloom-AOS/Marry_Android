package com.abloom.mery.data.firebase.announcements

import com.abloom.mery.data.firebase.documentsFlow
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AnnouncementDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun getLatestAnnouncementFlow(): Flow<AnnouncementDocument?> =
        db.collection(COLLECTION_ANNOUNCEMENT)
            .limit(1)
            .documentsFlow<AnnouncementDocument>()
            .map { it.firstOrNull() }
            .flowOn(Dispatchers.IO)

    companion object {
        private const val COLLECTION_ANNOUNCEMENT = "announcements"
    }
}
