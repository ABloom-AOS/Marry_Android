package com.abloom.mery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecommendationQuestionEntity::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class MeryDatabase : RoomDatabase() {

    abstract fun recommendationQuestionDao(): RecommendationQuestionDao
}
