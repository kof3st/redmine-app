package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account
import me.kofesst.android.redminecomposeapp.feature.data.storage.AppDatabase

class GetAccounts(
    private val database: AppDatabase,
) {

    suspend operator fun invoke(): List<Account> {
        return database.getAccountsDao().getAll()
    }
}