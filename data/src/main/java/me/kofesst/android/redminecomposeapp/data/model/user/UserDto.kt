package me.kofesst.android.redminecomposeapp.data.model.user

import me.kofesst.android.redminecomposeapp.domain.model.CurrentUser

data class UserDto(
    val id: Int,
    val mail: String,
) {
    fun toCurrentUser(host: String, apiKey: String): CurrentUser {
        return CurrentUser(id, mail, host, apiKey)
    }
}