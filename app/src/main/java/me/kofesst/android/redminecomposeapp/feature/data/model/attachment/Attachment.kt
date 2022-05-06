package me.kofesst.android.redminecomposeapp.feature.data.model.attachment

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Author
import me.kofesst.android.redminecomposeapp.feature.data.repository.util.DateTime

data class Attachment(
    val author: Author,
    val content_type: String,
    val content_url: String,
    val created_on: DateTime,
    val description: String,
    val filename: String,
    val filesize: Int,
    val id: Int
)