package me.kofesst.android.redminecomposeapp.feature.data.model.user

import me.kofesst.android.redminecomposeapp.feature.domain.model.CurrentUser

data class User(
    val id: Int,
    val mail: String
) {
    fun toCurrentUser(host: String, apiKey: String): CurrentUser {
        return CurrentUser(id, mail, host, apiKey)
    }
}