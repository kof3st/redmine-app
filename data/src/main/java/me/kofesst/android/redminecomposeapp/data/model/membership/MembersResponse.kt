package me.kofesst.android.redminecomposeapp.data.model.membership

data class MembersResponse(
    val limit: Int,
    val memberships: List<MembershipDto>,
    val offset: Int,
    val total_count: Int,
)