package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class SaveSession(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(host: String, apiKey: String) {
        repository.saveSession(host, apiKey)
    }
}