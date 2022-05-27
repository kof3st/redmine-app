package me.kofesst.android.redminecomposeapp.domain.model

import java.util.*

data class Project(
    val id: Int,
    val name: String,
    val identifier: String,
    val description: String,
    val createdOn: Date,
    val updatedOn: Date,
    val customFields: List<CustomField>,
)