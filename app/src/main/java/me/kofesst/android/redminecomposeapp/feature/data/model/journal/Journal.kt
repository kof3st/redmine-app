package me.kofesst.android.redminecomposeapp.feature.data.model.journal

data class Journal(
    val created_on: String,
    val details: List<Detail>,
    val id: Int,
    val notes: String?,
//    val user: User
)