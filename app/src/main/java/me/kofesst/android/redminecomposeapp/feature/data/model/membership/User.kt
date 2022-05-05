package me.kofesst.android.redminecomposeapp.feature.data.model.membership

data class User(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}