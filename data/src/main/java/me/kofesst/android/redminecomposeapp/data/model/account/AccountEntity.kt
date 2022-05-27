package me.kofesst.android.redminecomposeapp.data.model.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.kofesst.android.redminecomposeapp.domain.model.Account

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String = "",
    val host: String = "",
    val apiKey: String = "",
) {
    companion object {
        fun fromAccount(account: Account) = AccountEntity(
            id = account.id,
            name = account.name,
            host = account.host,
            apiKey = account.apiKey
        )
    }

    fun toAccount() = Account(
        id = id,
        name = name,
        host = host,
        apiKey = apiKey
    )
}