package me.kofesst.android.redminecomposeapp.data.model.issue

import me.kofesst.android.redminecomposeapp.domain.model.IdName

data class ChildIssueDto(
    val id: Int,
    val subject: String,
    val tracker: TrackerDto,
) {
    fun toIdName() = IdName(
        id = id,
        name = subject
    )
}