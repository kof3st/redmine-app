package me.kofesst.android.redminecomposeapp.feature.data.model.journal

data class Detail(
    val name: String,
    val new_value: String,
    val old_value: String?,
    val `property`: String
)