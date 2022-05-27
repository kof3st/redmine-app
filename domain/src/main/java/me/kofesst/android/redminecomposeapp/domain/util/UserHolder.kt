package me.kofesst.android.redminecomposeapp.domain.util

import androidx.lifecycle.MutableLiveData
import me.kofesst.android.redminecomposeapp.domain.model.CurrentUser

class UserHolder {
    var currentUser = MutableLiveData<CurrentUser?>(null)

    fun requireUser(): CurrentUser = currentUser.value!!

    val host: String get() = currentUser.value?.host ?: "invalid"
    val apiKey: String get() = currentUser.value?.apiKey ?: "invalid"
}