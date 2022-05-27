package me.kofesst.android.redminecomposeapp.domain.model

data class Account(
    val id: Int = -1,
    var name: String,
    var host: String,
    var apiKey: String,
)