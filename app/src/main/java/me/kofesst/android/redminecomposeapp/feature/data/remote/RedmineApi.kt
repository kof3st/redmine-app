package me.kofesst.android.redminecomposeapp.feature.data.remote

import me.kofesst.android.redmineapp.feature.data.model.project.ProjectsResponse
import me.kofesst.android.redminecomposeapp.feature.data.model.user.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

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
}