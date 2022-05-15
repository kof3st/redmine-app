package me.kofesst.android.redminecomposeapp.feature.domain.usecase

data class UseCases(
    val getCurrentUser: GetCurrentUser,
    val getProjects: GetProjects,
    val getProjectIssues: GetProjectIssues,
    val getOwnedIssues: GetOwnedIssues,
    val getAssignedIssues: GetAssignedIssues,
    val getIssueDetails: GetIssueDetails,
    val createIssue: CreateIssue,
    val updateIssue: UpdateIssue,
    val getTrackers: GetTrackers,
    val getStatuses: GetStatuses,
    val getMembers: GetMembers,
    val validateForEmptyField: ValidateForEmptyField,
    val validateForNotNullField: ValidateForNotNullField,
    val getAccount: GetAccount,
    val getAccounts: GetAccounts,
    val addAccount: AddAccount,
    val updateAccount: UpdateAccount,
    val deleteAccount: DeleteAccount,
)
