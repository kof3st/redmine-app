package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

class SaveSession(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val SESSION_HOST = stringPreferencesKey("session-host")
        val SESSION_API_KEY = stringPreferencesKey("session-api-key")
    }

    suspend operator fun invoke(
        host: String,
        apiKey: String,
    ) {
        dataStore.edit { preferences ->
            preferences[SESSION_HOST] = host
            preferences[SESSION_API_KEY] = apiKey
        }
    }
}