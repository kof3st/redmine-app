package me.kofesst.android.redminecomposeapp.feature.data.model.project

import me.kofesst.android.redminecomposeapp.feature.data.model.CustomField
import me.kofesst.android.redminecomposeapp.feature.data.repository.util.DateTime
import java.util.*

data class Project(
    val created_on: DateTime,
    val custom_fields: List<CustomField>,
    val description: String,
    val id: Int,
    val identifier: String,
    val name: String,
    val updated_on: DateTime
)