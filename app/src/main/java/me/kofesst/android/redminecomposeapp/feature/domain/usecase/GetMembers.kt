package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.membership.Membership
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository

class GetMembers(
    private val repository: RedmineRepository
) {

    suspend operator fun invoke(projectId: Int): List<Membership> {
        return repository.getMembers(projectId)
    }
}