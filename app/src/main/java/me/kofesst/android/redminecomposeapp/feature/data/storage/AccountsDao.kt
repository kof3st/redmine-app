package me.kofesst.android.redminecomposeapp.feature.data.storage

import androidx.room.*
import me.kofesst.android.redminecomposeapp.feature.data.model.account.Account

@Dao
interface AccountsDao {

    @Query("SELECT * FROM account WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): Account?

    @Query("SELECT * FROM account")
    suspend fun getAll(): List<Account>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(account: Account)

    @Update
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(account: Account)
}