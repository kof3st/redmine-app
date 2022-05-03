package me.kofesst.android.redminecomposeapp.feature.domain.model

data class CurrentUser(
    val id: Int,
    val mail: String,
    val host: String,
    val apiKey: String
)