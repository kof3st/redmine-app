package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RestoreSession(
    private val dataStore: DataStore<Preferences>,
) {

    suspend operator fun invoke(): Pair<String, String>? {
        return dataStore.data.map { preferences ->
            val host = preferences[SaveSession.SESSION_HOST] ?: return@map null
            val apiKey = preferences[SaveSession.SESSION_API_KEY] ?: return@map null

            return@map host to apiKey
        }.firstOrNull()
    }
}