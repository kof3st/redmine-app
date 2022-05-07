package me.kofesst.android.redminecomposeapp.feature.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account

@Database(
    entities = [Account::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao
}