package me.kofesst.android.redminecomposeapp.domain.model

data class ItemsPage<Item : Any>(
    val items: List<Item>,
    val limit: Int,
    val offset: Int,
    val totalCount: Int,
)
