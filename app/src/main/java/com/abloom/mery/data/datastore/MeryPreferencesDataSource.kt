package com.abloom.mery.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeryPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mery")

    val loginUserId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            val key = stringPreferencesKey(KEY_LOGIN_USER_ID)
            preferences[key]
        }

    suspend fun updateLoginUserId(userId: String) {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(KEY_LOGIN_USER_ID)
            preferences[key] = userId
        }
    }

    suspend fun removeLoginUserId() {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(KEY_LOGIN_USER_ID)
            preferences.remove(key)
        }
    }

    companion object {

        private const val KEY_LOGIN_USER_ID = "user_id"
    }
}
