package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account
import me.kofesst.android.redminecomposeapp.feature.data.storage.AppDatabase

class DeleteAccount(
    private val database: AppDatabase,
) {

    suspend operator fun invoke(account: Account) {
        database.getAccountsDao().delete(account)
    }
}