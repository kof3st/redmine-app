package me.kofesst.android.redminecomposeapp.feature.data.model.issue

data class Tracker(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}