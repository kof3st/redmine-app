package me.kofesst.android.redminecomposeapp.feature.domain.usecase

data class UseCases(
    val getCurrentUser: GetCurrentUser,
    val getProjects: GetProjects,
    val getIssues: GetIssues,
    val getIssueDetails: GetIssueDetails,
    val validateForEmptyField: ValidateForEmptyField
)
