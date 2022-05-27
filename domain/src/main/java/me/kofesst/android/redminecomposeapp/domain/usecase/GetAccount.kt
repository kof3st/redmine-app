package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetAccount(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(id: Int): Account? {
        return repository.getAccount(id)
    }
}