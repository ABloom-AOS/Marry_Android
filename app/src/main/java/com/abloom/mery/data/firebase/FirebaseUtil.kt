package com.abloom.mery.data.firebase

import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Query
import dev.gitlive.firebase.firestore.QuerySnapshot
import dev.gitlive.firebase.firestore.WriteBatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend fun FirebaseFirestore.runBatch(func: WriteBatch.() -> Unit) {
    val batch = batch()
    batch.func()
    batch.commit()
}

suspend inline fun <reified R : Document> Query.fetchDocuments(): List<R> = get().fetchDocuments()

inline fun <reified R : Document> QuerySnapshot.fetchDocuments(): List<R> =
    documents.map { it.data() }

suspend inline fun <reified R : Document> DocumentReference.fetchDocument(): R? =
    get().fetchDocument()

inline fun <reified R : Document> DocumentSnapshot.fetchDocument(): R? =
    if (exists) data<R>() else null

inline fun <reified R : Document> Query.documentsFlow(): Flow<List<R>> = snapshots.documentsFlow()

inline fun <reified R : Document> Flow<QuerySnapshot>.documentsFlow(): Flow<List<R>> =
    map { it.documents.map { document -> document.data() } }

inline fun <reified R : Document> DocumentReference.documentFlow(): Flow<R?> =
    snapshots.documentFlow<R>()

inline fun <reified R : Document> Flow<DocumentSnapshot>.documentFlow(): Flow<R?> =
    map {
        if (it.exists) it.data<R>() else null
    }
