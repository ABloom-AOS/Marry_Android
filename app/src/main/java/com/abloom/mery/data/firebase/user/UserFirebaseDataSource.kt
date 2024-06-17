package com.abloom.mery.data.firebase.user

import com.abloom.mery.data.firebase.documentFlow
import com.abloom.mery.data.firebase.fetchDocument
import com.abloom.mery.data.firebase.fetchDocuments
import com.abloom.mery.data.firebase.toTimestamp
import com.google.firebase.messaging.FirebaseMessaging
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

    val loginUserIdFlow: Flow<String?> = auth.authStateChanged.map { it?.uid }

    /**
     * @return 로그인에 성공하면 [FirebaseUser] 반환
     */
    suspend fun loginByGoogle(token: String): FirebaseUser? = withContext(Dispatchers.IO) {
        val credential = GoogleAuthProvider.credential(token, null)
        auth.signInWithCredential(credential)
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
        }.getOrElse { error ->
            if (error is FirebaseAuthInvalidCredentialsException) null else throw error
        }?.user
    }

    suspend fun signUpByEmail(
        email: String,
        password: String,
    ): FirebaseUser? = withContext(Dispatchers.IO) {
        auth.createUserWithEmailAndPassword(email, password)
            .user
    }

    suspend fun isExist(userId: String): Boolean = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .get()
            .exists
    }

    suspend fun createUserDocument(userDocument: UserDocument) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userDocument.id)
            .set(userDocument)
    }

    fun getUserDocumentFlow(userId: String): Flow<UserDocument1?> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .documentFlow<UserDocument1>()
            .flowOn(Dispatchers.IO)

    suspend fun connect(user1Id: String, user2Id: String): Boolean = withContext(Dispatchers.IO) {
        val user1Ref = db.collection(COLLECTIONS_USER).document(user1Id)
        val user2Ref = db.collection(COLLECTIONS_USER).document(user2Id)
        db.runTransaction {
            val user1Document = get(user1Ref).fetchDocument<UserDocument1>()
                ?: return@runTransaction false
            val user2Document = get(user2Ref).fetchDocument<UserDocument1>()
                ?: return@runTransaction false

            // 둘 중 한 명이라도 이미 연결되어 있으면 false 반환
            if (user1Document.fiance != null || user2Document.fiance != null) return@runTransaction false

            update(user1Ref, UserDocument1::fiance.name to user2Id)
            update(user2Ref, UserDocument1::fiance.name to user1Id)
            true
        }
    }

    suspend fun getUserDocumentByInvitationCode(
        invitationCode: String
    ): UserDocument1? = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .where { UserDocument1::invitation_code.name equalTo invitationCode }
            .fetchDocuments<UserDocument1>()
            .firstOrNull()
    }

    suspend fun updateName(userId: String, name: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument1::name.name to name)
    }

    suspend fun updateMarriageDate(
        userId: String,
        marriageDate: LocalDate
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument1::marriage_date.name to marriageDate.toTimestamp())
    }

    suspend fun signOut() = withContext(Dispatchers.IO) { auth.signOut() }

    suspend fun delete(userId: String) = withContext(Dispatchers.IO) {
        val userRef = db.collection(COLLECTIONS_USER)
            .document(userId)
        val answerDocumentRefs = userRef.collection(COLLECTIONS_ANSWER)
            .get()
            .fetchDocuments<UserDocument1>()
            .map { userRef.collection(COLLECTIONS_ANSWER).document(it.user_id) }

        db.runTransaction {
            val userDocument = get(userRef).fetchDocument<UserDocument1>() ?: return@runTransaction
            if (userDocument.fiance != null) {
                val fianceRef = db.collection(COLLECTIONS_USER).document(userDocument.fiance)
                update(fianceRef, UserDocument1::fiance.name to null)
            }
            answerDocumentRefs.forEach { delete(it) }
            delete(userRef)
        }
        auth.currentUser?.delete()
    }

    suspend fun loginUpdateFcmToken(userId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument1::fcm_token.name to messaging.token.result.toString())
    }

    suspend fun logOutUpdateFcmToken(userId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .update(UserDocument1::fcm_token.name to null)
    }

    companion object {

        private const val COLLECTIONS_USER = "users"
        private const val COLLECTIONS_ANSWER = "answers"
    }
}
