package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class GetAccounts(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(): List<Account> {
        return repository.getAccounts()
    }
}