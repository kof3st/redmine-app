package me.kofesst.android.redminecomposeapp.feature.data.model.journal

import me.kofesst.android.redminecomposeapp.feature.data.repository.util.DateTime

data class Journal(
    val created_on: DateTime,
    val details: List<Detail>,
    val id: Int,
    val notes: String?,
    val user: User
)