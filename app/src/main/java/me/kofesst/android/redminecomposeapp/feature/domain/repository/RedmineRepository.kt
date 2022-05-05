package me.kofesst.android.redminecomposeapp.feature.domain.repository

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.CreateIssueBody
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Issue
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.Tracker
import me.kofesst.android.redminecomposeapp.feature.data.model.membership.Membership
import me.kofesst.android.redminecomposeapp.feature.data.model.project.Project
import me.kofesst.android.redminecomposeapp.feature.data.model.status.Status
import me.kofesst.android.redminecomposeapp.feature.domain.model.CurrentUser

interface RedmineRepository {

    suspend fun getCurrentUser(host: String, apiKey: String): CurrentUser

    suspend fun getProjects(): List<Project>

    suspend fun getIssues(): List<Issue>

    suspend fun getIssueDetails(issueId: Int): Issue

    suspend fun createIssue(issue: CreateIssueBody)

    suspend fun updateIssue(issueId: Int, issue: CreateIssueBody)

    suspend fun getTrackers(): List<Tracker>

    suspend fun getStatuses(): List<Status>

    suspend fun getMembers(projectId: Int): List<Membership>
}