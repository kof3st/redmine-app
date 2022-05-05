package me.kofesst.android.redminecomposeapp.feature.data.remote

import me.kofesst.android.redminecomposeapp.feature.data.model.issue.CreateIssueBody
import me.kofesst.android.redmineapp.feature.data.model.project.ProjectsResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.IssueDetailsResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.issue.IssuesResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.membership.MembersResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.status.StatusesResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.tracker.TrackersResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.user.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface RedmineApi {
    companion object {
        private const val API_KEY_HEADER = "X-Redmine-API-Key"
    }

    @GET("/users/current.json")
    suspend fun getCurrentUser(
        @Header(API_KEY_HEADER) apiKey: String
    ): Response<UserResponse>

    @GET("/projects.json")
    suspend fun getProjects(
        @Header(API_KEY_HEADER) apiKey: String
    ): Response<ProjectsResponse>

    @GET("/issues.json?status_id=*")
    suspend fun getIssues(
        @Header(API_KEY_HEADER) apiKey: String
    ): Response<IssuesResponse>

    @GET("/issues/{issueId}.json?include=children,attachments,journals")
    suspend fun getIssueDetails(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("issueId") issueId: Int
    ): Response<IssueDetailsResponse>

    @POST("/issues.json")
    suspend fun createIssue(
        @Header(API_KEY_HEADER) apiKey: String,
        @Body issue: CreateIssueBody
    )

    @PUT("/issues/{issueId}.json")
    suspend fun updateIssue(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("issueId") issueId: Int,
        @Body issue: CreateIssueBody
    )

    @GET("/trackers.json")
    suspend fun getTrackers(
        @Header(API_KEY_HEADER) apiKey: String
    ): Response<TrackersResponse>

    @GET("/issue_statuses.json")
    suspend fun getStatuses(
        @Header(API_KEY_HEADER) apiKey: String
    ): Response<StatusesResponse>

    @GET("/projects/{projectId}/memberships.json")
    suspend fun getMembers(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("projectId") projectId: Int
    ): Response<MembersResponse>
}