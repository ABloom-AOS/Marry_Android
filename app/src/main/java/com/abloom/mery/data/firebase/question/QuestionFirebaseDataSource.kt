package com.abloom.mery.data.firebase.question

import com.abloom.mery.data.firebase.documentFlow
import com.abloom.mery.data.firebase.documentsFlow
import com.abloom.mery.data.firebase.fetchDocument
import com.abloom.mery.data.firebase.fetchDocuments
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionFirebaseDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getEssentialQuestionIds(): List<Long> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_ESSENTIAL_QUESTIONS)
            .document(DOCUMENT_ESSENTIAL_QUESTIONS_ID)
            .fetchDocument<EssentialQuestionsDocument>()
            ?.essentials
            ?: emptyList()
    }

    fun getQuestionsFlow(): Flow<List<QuestionDocument>> = db.collection(COLLECTION_QUESTIONS)
        .documentsFlow<QuestionDocument>()
        .flowOn(Dispatchers.IO)

    suspend fun getQuestions(): List<QuestionDocument> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_QUESTIONS)
            .get()
            .fetchDocuments()
    }

    fun getQuestionFlow(questionId: Long): Flow<QuestionDocument?> =
        db.collection(COLLECTION_QUESTIONS)
            .document(questionId.toString())
            .documentFlow<QuestionDocument>()
            .flowOn(Dispatchers.IO)

    suspend fun getQuestion(questionId: Long): QuestionDocument? = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_QUESTIONS)
            .document(questionId.toString())
            .fetchDocument()
    }

    companion object {

        private const val COLLECTION_ESSENTIAL_QUESTIONS = "essentialQuestions"
        private const val DOCUMENT_ESSENTIAL_QUESTIONS_ID = "essentialQuestionsId"
        private const val COLLECTION_QUESTIONS = "questions"
    }
}
