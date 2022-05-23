package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

class ClearSession(
    private val dataStore: DataStore<Preferences>,
) {

    suspend operator fun invoke() {
        dataStore.edit { preferences ->
            preferences.remove(SaveSession.SESSION_HOST)
            preferences.remove(SaveSession.SESSION_API_KEY)
        }
    }
}