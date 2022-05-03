package me.kofesst.android.redminecomposeapp.feature.domain.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import me.kofesst.android.redminecomposeapp.feature.domain.model.CurrentUser

class UserHolder {
    var currentUser by mutableStateOf<CurrentUser?>(null)

    fun requireUser(): CurrentUser = currentUser!!

    val host: String get() = currentUser?.host ?: "invalid"
    val apiKey: String get() = currentUser?.apiKey ?: "invalid"
}