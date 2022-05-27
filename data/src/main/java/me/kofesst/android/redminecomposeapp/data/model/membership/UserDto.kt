package me.kofesst.android.redminecomposeapp.data.model.membership

data class UserDto(
    val id: Int,
    val name: String,
) {
    override fun toString(): String {
        return name
    }
}