package me.kofesst.android.redminecomposeapp.presentation.issue.edit

import java.io.File

data class FileData(
    val fileContent: ByteArray,
    val extension: String,
    val fileName: String,
    val type: String,
)