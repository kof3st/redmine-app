package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetTrackers(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(): List<Tracker> {
        return repository.getTrackers()
    }
}