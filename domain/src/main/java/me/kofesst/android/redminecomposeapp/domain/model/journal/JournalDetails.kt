package me.kofesst.android.redminecomposeapp.domain.model.journal

data class JournalDetails(
    val name: String,
    val newValue: String?,
    val oldValue: String?,
    val `property`: String,
)
