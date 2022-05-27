package me.kofesst.android.redminecomposeapp.domain.model.journal

import me.kofesst.android.redminecomposeapp.domain.model.IdName
import java.util.*

data class Journal(
    val id: Int,
    val details: List<JournalDetails>,
    val notes: String?,
    val author: IdName,
    val date: Date,
)
