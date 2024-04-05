package com.abloom.mery.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MeryPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    val loginUserId: Flow<String?> = dataStore.data
        .map { preferences ->
            val key = stringPreferencesKey(KEY_LOGIN_USER_ID)
            preferences[key]
        }

    suspend fun updateLoginUserId(userId: String) {
        dataStore.edit { preferences ->
            val key = stringPreferencesKey(KEY_LOGIN_USER_ID)
            preferences[key] = userId
        }
    }

    suspend fun removeLoginUserId() {
        dataStore.edit { preferences ->
            val key = stringPreferencesKey(KEY_LOGIN_USER_ID)
            preferences.remove(key)
        }
    }

    companion object {

        private const val KEY_LOGIN_USER_ID = "user_id"
    }
}
