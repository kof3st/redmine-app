package me.kofesst.android.redminecomposeapp.data.model.journal

import me.kofesst.android.redminecomposeapp.data.repository.util.DateTime
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.journal.Journal

data class JournalDto(
    val created_on: DateTime,
    val details: List<DetailDto>,
    val id: Int,
    val notes: String?,
    val user: UserDto,
) {
    fun toJournal() = Journal(
        id = id,
        details = details.map { it.toDetails() },
        notes = notes,
        author = IdName(user.id, user.name),
        date = created_on
    )
}