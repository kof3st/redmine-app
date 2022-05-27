package me.kofesst.android.redminecomposeapp.data.model.attachment

import me.kofesst.android.redminecomposeapp.data.model.issue.AuthorDto
import me.kofesst.android.redminecomposeapp.data.repository.util.DateTime
import me.kofesst.android.redminecomposeapp.domain.model.Attachment
import me.kofesst.android.redminecomposeapp.domain.model.IdName

data class AttachmentDto(
    val author: AuthorDto,
    val content_type: String,
    val content_url: String,
    val created_on: DateTime,
    val description: String,
    val filename: String,
    val filesize: Int,
    val id: Int,
) {
    fun toAttachment() = Attachment(
        id = id,
        author = IdName(author.id, author.name),
        url = content_url,
        createdOn = created_on,
        description = description,
        fileName = filename,
        fileSize = filesize
    )
}