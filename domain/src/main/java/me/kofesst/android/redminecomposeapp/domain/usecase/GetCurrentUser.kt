package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.CurrentUser
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetCurrentUser(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(
        host: String,
        apiKey: String,
    ): CurrentUser {
        return repository.getCurrentUser(host, apiKey)
    }
}