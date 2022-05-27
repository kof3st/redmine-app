package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class ClearSession(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke() {
        repository.clearSession()
    }
}