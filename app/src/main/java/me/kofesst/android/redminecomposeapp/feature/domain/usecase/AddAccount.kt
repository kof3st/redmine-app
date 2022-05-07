package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account
import me.kofesst.android.redminecomposeapp.feature.data.storage.AppDatabase

class AddAccount(
    private val database: AppDatabase,
) {

    suspend operator fun invoke(account: Account) {
        database.getAccountsDao().add(account)
    }
}