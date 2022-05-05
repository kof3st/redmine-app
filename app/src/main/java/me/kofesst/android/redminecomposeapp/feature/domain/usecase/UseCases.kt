package me.kofesst.android.redminecomposeapp.feature.domain.usecase

data class UseCases(
    val getCurrentUser: GetCurrentUser,
    val getProjects: GetProjects,
    val getIssues: GetIssues,
    val getIssueDetails: GetIssueDetails,
    val getTrackers: GetTrackers,
    val getStatuses: GetStatuses,
    val getMembers: GetMembers,
    val validateForEmptyField: ValidateForEmptyField,
    val validateForNotNullField: ValidateForNotNullField
)
