package me.kofesst.android.redminecomposeapp.data.remote

import me.kofesst.android.redminecomposeapp.data.model.attachment.UploadResponse
import me.kofesst.android.redminecomposeapp.data.model.issue.CreateIssueBody
import me.kofesst.android.redminecomposeapp.data.model.issue.IssueDetailsResponse
import me.kofesst.android.redminecomposeapp.data.model.issue.IssuesResponse
import me.kofesst.android.redminecomposeapp.data.model.membership.MembersResponse
import me.kofesst.android.redminecomposeapp.data.model.project.ProjectsResponse
import me.kofesst.android.redminecomposeapp.data.model.status.StatusesResponse
import me.kofesst.android.redminecomposeapp.data.model.tracker.TrackersResponse
import me.kofesst.android.redminecomposeapp.data.model.user.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RedmineApi {
    companion object {
        private const val API_KEY_HEADER = "X-Redmine-API-Key"
    }

    @GET("/users/current.json")
    suspend fun getCurrentUser(
        @Header(API_KEY_HEADER) apiKey: String,
    ): Response<UserResponse>

    @GET("/projects.json")
    suspend fun getProjects(
        @Header(API_KEY_HEADER) apiKey: String,
    ): Response<ProjectsResponse>

    @GET("/issues.json?status_id=*")
    suspend fun getProjectIssues(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("project_id") projectId: Int,
        @Query("offset") offset: Int,
    ): Response<IssuesResponse>

    @GET("/issues.json?status_id=*&author_id=me")
    suspend fun getOwnedIssues(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("offset") offset: Int,
    ): Response<IssuesResponse>

    @GET("/issues.json?status_id=open&assigned_to_id=me")
    suspend fun getAssignedIssues(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("offset") offset: Int,
    ): Response<IssuesResponse>

    @GET("/issues/{issueId}.json?include=children,attachments,journals")
    suspend fun getIssueDetails(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("issueId") issueId: Int,
    ): Response<IssueDetailsResponse>

    @Headers("Content-Type: application/xml")
    @POST("/issues.json")
    suspend fun createIssue(
        @Header(API_KEY_HEADER) apiKey: String,
        @Body issue: CreateIssueBody,
    )

    @Headers("Content-Type: application/xml")
    @PUT("/issues/{issueId}.json")
    suspend fun updateIssue(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("issueId") issueId: Int,
        @Body issue: CreateIssueBody,
    )

    @GET("/trackers.json")
    suspend fun getTrackers(
        @Header(API_KEY_HEADER) apiKey: String,
    ): Response<TrackersResponse>

    @GET("/issue_statuses.json")
    suspend fun getStatuses(
        @Header(API_KEY_HEADER) apiKey: String,
    ): Response<StatusesResponse>

    @GET("/projects/{projectId}/memberships.json")
    suspend fun getMembers(
        @Header(API_KEY_HEADER) apiKey: String,
        @Path("projectId") projectId: Int,
    ): Response<MembersResponse>

    @POST("/uploads.json")
    @Headers("Content-Type: application/octet-stream")
    suspend fun uploadFile(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("filename") filename: String,
        @Body file: RequestBody
    ): Response<UploadResponse>
}