package com.abloom.mery.data.firebase.user

import com.abloom.mery.data.firebase.toTimestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class UserFirebaseDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val messaging: FirebaseMessaging
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
            auth.signInWithEmailAndPassword(email, password).await()
        }.getOrElse { error ->
            if (error is FirebaseAuthInvalidCredentialsException) null else throw error
        }?.user
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

    fun getUserDocumentFlow(userId: String): Flow<UserDocument?> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .snapshots()
            .map { snapshot ->
                if (!snapshot.exists()) return@map null
                snapshot.toObject(UserDocument::class.java)
            }
            .flowOn(Dispatchers.IO)

    suspend fun connect(user1Id: String, user2Id: String): Boolean = withContext(Dispatchers.IO) {
        val user1Ref = db.collection(COLLECTIONS_USER).document(user1Id)
        val user2Ref = db.collection(COLLECTIONS_USER).document(user2Id)
        db.runTransaction { transaction ->
            val user1Document = transaction.get(user1Ref)
                .toObject<UserDocument>()
                ?: return@runTransaction false
            val user2Document = transaction.get(user2Ref)
                .toObject<UserDocument>()
                ?: return@runTransaction false

            if (user1Document.fianceId != null || user2Document.fianceId != null) return@runTransaction false

            transaction.update(user1Ref, UserDocument.KEY_FIANCE, user2Id)
            transaction.update(user2Ref, UserDocument.KEY_FIANCE, user1Id)
            true
        }.await()
    }

    suspend fun getUserDocumentByInvitationCode(
        invitationCode: String
    ): UserDocument? = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .whereEqualTo(UserDocument.KEY_INVITATION_CODE, invitationCode)
            .get()
            .await()
            .firstOrNull()
            ?.toObject()
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
        val userRef = db.collection(COLLECTIONS_USER)
            .document(userId)
        val answerDocumentRefs = userRef.collection(COLLECTIONS_ANSWER)
            .get()
            .await()
            .documents
            .map { userRef.collection(COLLECTIONS_ANSWER).document(it.id) }

        db.runTransaction { transaction ->
            val userDocument =
                transaction.get(userRef).toObject<UserDocument>() ?: return@runTransaction
            if (userDocument.fianceId != null) {
                val fianceRef = db.collection(COLLECTIONS_USER).document(userDocument.fianceId)
                transaction.update(fianceRef, UserDocument.KEY_FIANCE, null)
            }
            answerDocumentRefs.forEach { transaction.delete(it) }
            transaction.delete(userRef)
            auth.currentUser?.delete()
        }
    }

    suspend fun loginUpdateFcmToken(userId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument.KEY_FCM_TOKEN, messaging.token.result.toString())
    }

    suspend fun logOutUpdateFcmToken(userId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument.KEY_FCM_TOKEN, null)
    }

    companion object {

        private const val COLLECTIONS_USER = "users"
        private const val COLLECTIONS_ANSWER = "answers"
    }
}
