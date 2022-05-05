package me.kofesst.android.redminecomposeapp.feature.data.model.membership

data class MembersResponse(
    val limit: Int,
    val memberships: List<Membership>,
    val offset: Int,
    val total_count: Int
)