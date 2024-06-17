package com.abloom.mery.data.firebase.qna

import com.abloom.mery.data.firebase.documentsFlow
import com.abloom.mery.data.firebase.fetchDocument
import com.google.firebase.firestore.QuerySnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QnaFirebaseDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun getQnaDocumentsFlow(userId: String): Flow<List<QnaDocument1>> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .collection(COLLECTIONS_ANSWERS)
            .documentsFlow<QnaDocument1>()
            .flowOn(Dispatchers.IO)

    fun getQnaDocumentFlow(userId: String, questionId: Long): Flow<QnaDocument1?> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .collection(COLLECTIONS_ANSWERS)
            .where { QnaDocument1::q_id.name equalTo questionId }
            .documentsFlow<QnaDocument1>()
            .map { it.firstOrNull() }
            .flowOn(Dispatchers.IO)

    suspend fun createQnaDocument(qnaDocument: QnaDocument1) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(qnaDocument.user_id)
            .collection(COLLECTIONS_ANSWERS)
            .add(qnaDocument)
    }

    suspend fun updateReaction(
        loginUserId: String,
        fianceId: String,
        questionId: Long,
        reaction: Int
    ) = withContext(Dispatchers.IO) {
        val loginUserQnaQueryAsync = getQnaDocumentAsync(loginUserId, questionId)
        val fianceQnaQueryAsync = getQnaDocumentAsync(fianceId, questionId)

        val (loginUserQnaQuerySnapshot, fianceQnaQuerySnapshot) =
            awaitAll(loginUserQnaQueryAsync, fianceQnaQueryAsync)

        val loginUserAnswerId = loginUserQnaQuerySnapshot.firstOrNull()?.id ?: return@withContext
        val fianceAnswerId = fianceQnaQuerySnapshot.firstOrNull()?.id ?: return@withContext

        val loginUserAnswerRef = db.collection(COLLECTIONS_USER).document(loginUserId)
            .collection(COLLECTIONS_ANSWERS)
            .document(loginUserAnswerId)
        val fianceAnswerRef = db.collection(COLLECTIONS_USER).document(fianceId)
            .collection(COLLECTIONS_ANSWERS)
            .document(fianceAnswerId)

        db.runTransaction {
            val fianceQnaDocument = get(fianceAnswerRef).fetchDocument<QnaDocument1>()
                ?: return@runTransaction

            val willQnaComplete =
                reaction.isPositiveResponse() && fianceQnaDocument.reaction?.isPositiveResponse() ?: false

            if (willQnaComplete) {
                update(
                    loginUserAnswerRef,
                    QnaDocument1::reaction.name to reaction,
                    QnaDocument1::is_complete.name to true
                )
                update(
                    fianceAnswerRef,
                    QnaDocument1::is_complete.name to true
                )
            } else {
                update(
                    loginUserAnswerRef,
                    QnaDocument1::reaction.name to reaction
                )
            }
        }
    }

    private fun getQnaDocumentAsync(
        userId: String,
        questionId: Long
    ): Deferred<QuerySnapshot> = db.collection(COLLECTIONS_USER)
        .document(userId)
        .collection(COLLECTIONS_ANSWERS)
        .where { QnaDocument1::q_id.name equalTo questionId }
        .android
        .get()
        .asDeferred()

    companion object {

        private const val COLLECTIONS_USER = "users"
        private const val COLLECTIONS_ANSWERS = "answers"
    }
}
