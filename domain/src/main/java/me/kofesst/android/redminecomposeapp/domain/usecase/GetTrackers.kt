package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetTrackers(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(): List<IdName> {
        return repository.getTrackers()
    }
}