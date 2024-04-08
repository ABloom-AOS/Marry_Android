package com.abloom.mery.data.firebase.question

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionFirebaseDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getEssentialQuestionIds(): List<Long> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_ESSENTIAL_QUESTIONS)
            .document(DOCUMENT_ESSENTIAL_QUESTIONS_ID)
            .get()
            .await()
            .let { snapshot ->
                val essentials = snapshot[FIELD_ESSENTIALS]
                if (essentials !is List<*>) return@let emptyList<Long>()
                essentials.filterIsInstance<Long>()
            }
    }

    fun getQuestionsFlow(): Flow<List<QuestionDocument>> = db.collection(COLLECTION_QUESTIONS)
        .snapshots()
        .map { snapshot -> snapshot.toObjects(QuestionDocument::class.java) }
        .flowOn(Dispatchers.IO)

    suspend fun getQuestions(): List<QuestionDocument> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_QUESTIONS)
            .get()
            .await()
            .toObjects(QuestionDocument::class.java)
    }

    fun getQuestionFlow(questionId: Long): Flow<QuestionDocument?> =
        db.collection(COLLECTION_QUESTIONS)
            .document(questionId.toString())
            .snapshots()
            .map { snapshot -> snapshot.toObject(QuestionDocument::class.java) }
            .flowOn(Dispatchers.IO)

    suspend fun getQuestion(questionId: Long): QuestionDocument? = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_QUESTIONS)
            .document(questionId.toString())
            .get()
            .await()
            .toObject(QuestionDocument::class.java)
    }

    companion object {

        private const val COLLECTION_ESSENTIAL_QUESTIONS = "essentialQuestions"
        private const val DOCUMENT_ESSENTIAL_QUESTIONS_ID = "essentialQuestionsId"
        private const val FIELD_ESSENTIALS = "essentials"
        private const val COLLECTION_QUESTIONS = "questions"
    }
}
