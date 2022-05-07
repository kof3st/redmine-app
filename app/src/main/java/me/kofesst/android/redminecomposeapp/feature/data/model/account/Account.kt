package me.kofesst.android.redminecomposeapp.feature.data.model.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String = "",
    val host: String = "",
    val apiKey: String = "",
)
