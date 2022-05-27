package me.kofesst.android.redminecomposeapp.data.model.membership

import me.kofesst.android.redminecomposeapp.data.model.issue.ProjectDto
import me.kofesst.android.redminecomposeapp.domain.model.IdName
import me.kofesst.android.redminecomposeapp.domain.model.ProjectMember

data class MembershipDto(
    val id: Int,
    val project: ProjectDto,
    val roles: List<RoleDto>,
    val user: UserDto,
) {
    fun toMember() = ProjectMember(
        id = id,
        projectId = project.id,
        user = IdName(user.id, user.name)
    )
}