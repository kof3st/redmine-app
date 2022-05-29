package me.kofesst.android.redminecomposeapp.domain.model

data class Account(
    val id: Int = 0,
    var name: String,
    var host: String,
    var apiKey: String,
)