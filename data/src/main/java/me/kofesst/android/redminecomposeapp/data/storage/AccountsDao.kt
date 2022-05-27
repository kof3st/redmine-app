package me.kofesst.android.redminecomposeapp.data.storage

import androidx.room.*
import me.kofesst.android.redminecomposeapp.data.model.account.AccountEntity

@Dao
interface AccountsDao {

    @Query("SELECT * FROM account WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): AccountEntity?

    @Query("SELECT * FROM account")
    suspend fun getAll(): List<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)
}