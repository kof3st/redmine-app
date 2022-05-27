package me.kofesst.android.redminecomposeapp.domain.usecase

import me.kofesst.android.redminecomposeapp.domain.model.Account
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository

class UpdateAccount(
    private val repository: RedmineRepository,
) {

    suspend operator fun invoke(account: Account) {
        repository.updateAccount(account)
    }
}