package me.kofesst.android.redminecomposeapp.data.model.project

import me.kofesst.android.redminecomposeapp.data.model.CustomFieldDto
import me.kofesst.android.redminecomposeapp.data.repository.util.DateTime
import me.kofesst.android.redminecomposeapp.domain.model.Project

data class ProjectDto(
    val created_on: DateTime,
    val custom_fields: List<CustomFieldDto>,
    val description: String,
    val id: Int,
    val identifier: String,
    val name: String,
    val updated_on: DateTime,
) {
    fun toProject() = Project(
        id = id,
        name = name,
        identifier = identifier,
        description = description,
        createdOn = created_on,
        updatedOn = updated_on,
        customFields = custom_fields.map { it.toCustomField() }
    )
}