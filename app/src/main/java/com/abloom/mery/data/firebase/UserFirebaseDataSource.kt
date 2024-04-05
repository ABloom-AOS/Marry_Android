package com.abloom.mery.data.firebase

import com.abloom.mery.data.di.ApplicationScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirebaseDataSource @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    val loginUserId: String?
        get() = auth.currentUser?.uid

    /**
     * @return 로그인에 성공하면 [FirebaseUser] 반환
     */
    suspend fun loginByGoogle(token: String): FirebaseUser? = withContext(Dispatchers.IO) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .await()
            .user
    }

    /**
     * @return 로그인에 성공하면 [FirebaseUser] 반환
     */
    suspend fun loginByEmail(
        email: String,
        password: String
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        runCatching {
            auth.signInWithEmailAndPassword(email, password)
        }.getOrNull()
            ?.await()
            ?.user
    }

    suspend fun signUpByEmail(
        email: String,
        password: String,
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        auth.createUserWithEmailAndPassword(email, password)
            .await()
            .user
    }

    suspend fun isExist(userId: String): Boolean = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .get()
            .await()
            .exists()
    }

    suspend fun createUserDocument(userDocument: UserDocument) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userDocument.id)
            .set(userDocument)
    }

    suspend fun getUserDocument(userId: String): UserDocument? = db.collection(COLLECTIONS_USER)
        .document(userId)
        .get()
        .await()
        .toObject(UserDocument::class.java)

    fun getUserDocumentFlow(userId: String): Flow<UserDocument?> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .snapshots()
            .map { snapshot ->
                if (!snapshot.exists()) return@map null
                snapshot.toObject(UserDocument::class.java)
            }
            .flowOn(Dispatchers.IO)

    suspend fun getUserDocumentByInvitationCode(invitationCode: String): UserDocument? =
        db.collection(COLLECTIONS_USER)
            .whereEqualTo(UserDocument.KEY_INVITATION_CODE, invitationCode)
            .get()
            .await()
            .toObjects(UserDocument::class.java)
            .firstOrNull()

    suspend fun updateFianceId(userId: String, fianceId: String?) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument.KEY_FIANCE, fianceId)
    }

    suspend fun updateName(userId: String, name: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument.KEY_NAME, name)
    }

    suspend fun updateMarriageDate(
        userId: String,
        marriageDate: LocalDate
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument.KEY_MARRIAGE_DATE, marriageDate.toTimestamp())
    }

    suspend fun signOut() = withContext(Dispatchers.IO) { auth.signOut() }

    suspend fun delete(userId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .delete()
        auth.currentUser?.delete()
    }

    companion object {

        private const val COLLECTIONS_USER = "users"
    }
}
