package me.kofesst.android.redminecomposeapp.feature.data.model.issue

data class ChildIssue(
    val id: Int,
    val subject: String,
    val tracker: Tracker
)