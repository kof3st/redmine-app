package me.kofesst.android.redminecomposeapp.domain.model

data class UploadData(
    val token: String,
    val fileName: String,
    val type: String,
)