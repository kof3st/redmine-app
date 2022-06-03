package me.kofesst.android.redminecomposeapp.domain.usecase

data class UseCases(
    val getCurrentUser: GetCurrentUser,
    val getProjects: GetProjects,
    val getIssues: GetIssues,
    val getIssueDetails: GetIssueDetails,
    val createIssue: CreateIssue,
    val updateIssue: UpdateIssue,
    val getTrackers: GetTrackers,
    val getStatuses: GetStatuses,
    val getMembers: GetMembers,
    val uploadFile: UploadFile,
    val validateForEmptyField: ValidateForEmptyField,
    val validateForNotNullField: ValidateForNotNullField,
    val getAccount: GetAccount,
    val getAccounts: GetAccounts,
    val addAccount: AddAccount,
    val updateAccount: UpdateAccount,
    val deleteAccount: DeleteAccount,
    val saveSession: SaveSession,
    val restoreSession: RestoreSession,
    val clearSession: ClearSession,
)
