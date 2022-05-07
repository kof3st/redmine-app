package me.kofesst.android.redminecomposeapp.feature.domain.usecase

import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account
import me.kofesst.android.redminecomposeapp.feature.data.storage.AppDatabase

class GetAccount(
    private val database: AppDatabase,
) {

    suspend operator fun invoke(id: Int): Account? {
        return database.getAccountsDao().get(id)
    }
}