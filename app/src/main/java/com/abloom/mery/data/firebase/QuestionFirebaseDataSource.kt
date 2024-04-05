package com.abloom.mery.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestionFirebaseDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getEssentialQuestions(): Flow<List<QuestionDocument>> =
        db.collection(COLLECTION_ESSENTIAL_QUESTIONS)
            .document(DOCUMENT_ESSENTIAL_QUESTIONS_ID)
            .snapshots()
            .map { snapshot ->
                val essentials = snapshot[FIELD_ESSENTIALS]
                if (essentials !is List<*>) return@map emptyList<Long>()
                essentials.filterIsInstance<Long>()
            }.flatMapLatest { ids -> getQuestionsByIds(ids) }
            .flowOn(Dispatchers.IO)

    private fun getQuestionsByIds(
        ids: List<Long>
    ): Flow<List<QuestionDocument>> = getQuestions()
        .map { questions -> questions.filter { it.id in ids } }

    fun getQuestions(): Flow<List<QuestionDocument>> = db.collection(COLLECTION_QUESTIONS)
        .snapshots()
        .map { snapshot -> snapshot.toObjects(QuestionDocument::class.java) }
        .flowOn(Dispatchers.IO)

    fun getQuestion(questionId: Long): Flow<QuestionDocument> = db.collection(COLLECTION_QUESTIONS)
        .document(questionId.toString())
        .snapshots()
        .map { snapshot ->
            snapshot.toObject(QuestionDocument::class.java)
                ?: throw NoSuchElementException("질문 아이디가 ${questionId}인 질문이 존재하지 않습니다.")
        }
        .flowOn(Dispatchers.IO)

    companion object {

        private const val COLLECTION_ESSENTIAL_QUESTIONS = "essentialQuestions"
        private const val DOCUMENT_ESSENTIAL_QUESTIONS_ID = "essentialQuestionsId"
        private const val FIELD_ESSENTIALS = "essentials"
        private const val COLLECTION_QUESTIONS = "questions"
    }
}
