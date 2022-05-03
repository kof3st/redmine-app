package me.kofesst.android.redminecomposeapp.feature.data.model.attachment

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Author

data class Attachment(
    val author: Author,
    val content_type: String,
    val content_url: String,
    val created_on: String,
    val description: String,
    val filename: String,
    val filesize: Int,
    val id: Int
)