package me.kofesst.android.redminecomposeapp.feature.data.model.project

import me.kofesst.android.redminecomposeapp.feature.data.model.CustomField
import java.util.*

data class Project(
    val created_on: Date,
    val custom_fields: List<CustomField>,
    val description: String,
    val id: Int,
    val identifier: String,
    val name: String,
    val updated_on: String
)