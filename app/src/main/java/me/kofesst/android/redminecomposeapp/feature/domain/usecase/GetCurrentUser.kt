package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.domain.model.CurrentUser
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetCurrentUser(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(
        host: String,
        apiKey: String
    ): CurrentUser {
        return repository.getCurrentUser(host, apiKey)
    }
}