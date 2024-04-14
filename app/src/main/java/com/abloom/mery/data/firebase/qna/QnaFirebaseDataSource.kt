package com.abloom.mery.data.firebase.qna

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
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

    fun getQnaDocumentsFlow(userId: String): Flow<List<QnaDocument>> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .collection(COLLECTIONS_ANSWERS)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(QnaDocument::class.java) }
            .flowOn(Dispatchers.IO)

    fun getQnaDocumentFlow(userId: String, questionId: Long): Flow<QnaDocument?> =
        db.collection(COLLECTIONS_USER)
            .document(userId)
            .collection(COLLECTIONS_ANSWERS)
            .whereEqualTo(QnaDocument.KEY_QUESTION_ID, questionId)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(QnaDocument::class.java).firstOrNull() }
            .flowOn(Dispatchers.IO)

    suspend fun createQnaDocument(qnaDocument: QnaDocument) = withContext(Dispatchers.IO) {
        db.collection(COLLECTIONS_USER)
            .document(qnaDocument.userId)
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

        db.runTransaction { transaction ->
            val fianceQnaDocument = transaction.get(fianceAnswerRef)
                .toObject<QnaDocument>()
                ?: return@runTransaction

            val isComplete =
                reaction.isPositiveResponse() && fianceQnaDocument.reaction?.isPositiveResponse() ?: false

            if (isComplete) {
                transaction.update(
                    loginUserAnswerRef,
                    QnaDocument.KEY_REACTION, reaction,
                    QnaDocument.KEY_IS_COMPLETE, true
                )
                transaction.update(
                    fianceAnswerRef,
                    QnaDocument.KEY_IS_COMPLETE, true
                )
            } else {
                transaction.update(
                    loginUserAnswerRef,
                    QnaDocument.KEY_REACTION, reaction,
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
        .whereEqualTo(QnaDocument.KEY_QUESTION_ID, questionId)
        .get()
        .asDeferred()

    companion object {

        private const val COLLECTIONS_USER = "users"
        private const val COLLECTIONS_ANSWERS = "answers"
    }
}
