package com.abloom.mery.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMeryDatabase(
        @ApplicationContext context: Context,
    ): MeryDatabase = Room.databaseBuilder(
        context = context,
        klass = MeryDatabase::class.java,
        name = "mery-database"
    ).build()

    @Provides
    fun providesRecommendationQuestionDao(
        database: MeryDatabase,
    ): RecommendationQuestionDao = database.recommendationQuestionDao()
}
