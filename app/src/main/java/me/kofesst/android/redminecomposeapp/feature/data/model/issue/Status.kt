package me.kofesst.android.redminecomposeapp.feature.data.model.issue

data class Status(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}