package me.kofesst.android.redminecomposeapp.data.model.status

data class StatusDto(
    val id: Int,
    val name: String,
) {
    override fun toString(): String {
        return name
    }
}