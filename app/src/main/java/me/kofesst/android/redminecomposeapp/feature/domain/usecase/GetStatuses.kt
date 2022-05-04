package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetStatuses(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(): List<Status> {
        return repository.getStatuses()
    }
}