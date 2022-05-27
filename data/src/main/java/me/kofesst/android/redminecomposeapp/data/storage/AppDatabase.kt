package me.kofesst.android.redminecomposeapp.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import me.kofesst.android.redminecomposeapp.data.model.account.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao
}