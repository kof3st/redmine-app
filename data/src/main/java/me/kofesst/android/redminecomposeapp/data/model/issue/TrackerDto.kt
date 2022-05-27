package me.kofesst.android.redminecomposeapp.data.model.issue

data class TrackerDto(
    val id: Int,
    val name: String,
) {
    override fun toString(): String {
        return name
    }
}