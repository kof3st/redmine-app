package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class RestoreSession(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(): Pair<String, String>? {
        return repository.restoreSession()
    }
}