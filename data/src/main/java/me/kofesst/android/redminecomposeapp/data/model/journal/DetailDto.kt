package me.kofesst.android.redminecomposeapp.data.model.journal

import me.kofesst.android.redminecomposeapp.domain.model.journal.JournalDetails

data class DetailDto(
    val name: String,
    val new_value: String?,
    val old_value: String?,
    val `property`: String,
) {
    fun toDetails() = JournalDetails(
        name = name,
        newValue = new_value,
        oldValue = old_value,
        property = property
    )
}