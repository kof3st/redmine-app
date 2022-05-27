package me.kofesst.android.redminecomposeapp.domain.model

import java.util.*

data class Attachment(
    val id: Int,
    val author: IdName,
    val url: String,
    val createdOn: Date,
    val description: String,
    val fileName: String,
    val fileSize: Int,
)