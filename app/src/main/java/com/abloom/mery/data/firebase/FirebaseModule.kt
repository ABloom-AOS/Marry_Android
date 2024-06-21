package com.abloom.mery.data.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth =
        dev.gitlive.firebase.Firebase.auth

    @Singleton
    @Provides
    fun providesFirebaseDb(): FirebaseFirestore =
        dev.gitlive.firebase.Firebase.firestore

    @Singleton
    @Provides
    fun providerFirebaseMessaging(): FirebaseMessaging = Firebase.messaging
}
