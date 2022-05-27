package me.kofesst.android.redminecomposeapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import me.kofesst.android.redminecomposeapp.data.model.account.AccountEntity
import me.kofesst.android.redminecomposeapp.data.model.issue.CreateIssueBody
import me.kofesst.android.redminecomposeapp.data.remote.RedmineApi
import me.kofesst.android.redminecomposeapp.data.repository.util.DateDeserializer
import me.kofesst.android.redminecomposeapp.data.repository.util.DateTime
import me.kofesst.android.redminecomposeapp.data.repository.util.DateTimeDeserializer
import me.kofesst.android.redminecomposeapp.data.storage.AppDatabase
import me.kofesst.android.redminecomposeapp.domain.model.*
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository
import me.kofesst.android.redminecomposeapp.domain.util.UserHolder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.UnknownHostException
import java.util.*

class RedmineRepositoryImpl(
    private val userHolder: UserHolder,
    private val database: AppDatabase,
    private val dataStore: DataStore<Preferences>,
) : RedmineRepository {
    companion object {
        val SESSION_HOST = stringPreferencesKey("session-host")
        val SESSION_API_KEY = stringPreferencesKey("session-api-key")
    }

    @Throws(Exception::class)
    override suspend fun getCurrentUser(host: String, apiKey: String): CurrentUser {
        return handleResponse(host) { api ->
            api.getCurrentUser(apiKey)
        }.user.toCurrentUser(host, apiKey)
    }

    @Throws(Exception::class)
    override suspend fun getProjects(): List<Project> {
        return handleResponse(userHolder.host) { api ->
            api.getProjects(userHolder.apiKey)
        }.projects.map { it.toProject() }
    }

    @Throws(Exception::class)
    override suspend fun getProjectIssues(projectId: Int, offset: Int): ItemsPage<Issue> {
        return handleResponse(userHolder.host) { api ->
            api.getProjectIssues(
                apiKey = userHolder.apiKey,
                projectId = projectId,
                offset = offset
            )
        }.toPage()
    }

    @Throws(Exception::class)
    override suspend fun getOwnedIssues(offset: Int): ItemsPage<Issue> {
        return handleResponse(userHolder.host) { api ->
            api.getOwnedIssues(
                apiKey = userHolder.apiKey,
                offset = offset
            )
        }.toPage()
    }

    @Throws(Exception::class)
    override suspend fun getAssignedIssues(offset: Int): ItemsPage<Issue> {
        return handleResponse(userHolder.host) { api ->
            api.getAssignedIssues(
                apiKey = userHolder.apiKey,
                offset = offset
            )
        }.toPage()
    }

    @Throws(Exception::class)
    override suspend fun getIssueDetails(issueId: Int): Issue {
        return handleResponse(userHolder.host) { api ->
            api.getIssueDetails(userHolder.apiKey, issueId)
        }.issue.toIssue()
    }

    @Throws(Exception::class)
    override suspend fun createIssue(
        issue: Issue,
        attachments: List<UploadData>,
    ) {
        handleRequest(
            host = userHolder.host,
            useXml = true
        ) { api ->
            api.createIssue(userHolder.apiKey, CreateIssueBody.fromIssue(issue, attachments))
        }
    }

    @Throws(Exception::class)
    override suspend fun updateIssue(
        issueId: Int,
        issue: Issue,
        attachments: List<UploadData>,
        notes: String?,
    ) {
        handleRequest(
            host = userHolder.host,
            useXml = true
        ) { api ->
            api.updateIssue(userHolder.apiKey, issueId, CreateIssueBody.fromIssue(
                issue = issue,
                attachments = attachments,
                notes = notes
            ))
        }
    }

    @Throws(Exception::class)
    override suspend fun getTrackers(): List<IdName> {
        return handleResponse(userHolder.host) { api ->
            api.getTrackers(userHolder.apiKey)
        }.trackers.map { IdName(it.id, it.name) }
    }

    @Throws(Exception::class)
    override suspend fun getStatuses(): List<IdName> {
        return handleResponse(userHolder.host) { api ->
            api.getStatuses(userHolder.apiKey)
        }.issue_statuses.map { IdName(it.id, it.name) }
    }

    @Throws(Exception::class)
    override suspend fun getMembers(projectId: Int): List<ProjectMember> {
        return handleResponse(userHolder.host) { api ->
            api.getMembers(userHolder.apiKey, projectId)
        }.memberships.map { it.toMember() }
    }

    override suspend fun uploadFile(file: File, type: String): String {
        return handleResponse(userHolder.host) { api ->
            val requestFile = RequestBody.create(
                MediaType.parse(type),
                file
            )

            val body = MultipartBody.Part.createFormData(
                "",
                file.name,
                requestFile
            )

            api.uploadFile(userHolder.apiKey, file.name, body)
        }.upload.token
    }

    override suspend fun addAccount(account: Account) {
        database.getAccountsDao().add(AccountEntity.fromAccount(account))
    }

    override suspend fun getAccount(id: Int): Account? {
        return database.getAccountsDao().get(id)?.toAccount()
    }

    override suspend fun getAccounts(): List<Account> {
        return database.getAccountsDao().getAll().map { it.toAccount() }
    }

    override suspend fun updateAccount(account: Account) {
        database.getAccountsDao().update(AccountEntity.fromAccount(account))
    }

    override suspend fun deleteAccount(account: Account) {
        database.getAccountsDao().delete(AccountEntity.fromAccount(account))
    }

    override suspend fun saveSession(host: String, apiKey: String) {
        dataStore.edit { preferences ->
            preferences[SESSION_HOST] = host
            preferences[SESSION_API_KEY] = apiKey
        }
    }

    override suspend fun restoreSession(): Pair<String, String>? {
        return dataStore.data.map { preferences ->
            val host = preferences[SESSION_HOST] ?: return@map null
            val apiKey = preferences[SESSION_API_KEY] ?: return@map null

            return@map host to apiKey
        }.firstOrNull()
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_HOST)
            preferences.remove(SESSION_API_KEY)
        }
    }

    @Throws(Exception::class)
    private suspend fun handleRequest(
        host: String,
        useXml: Boolean = false,
        request: suspend (RedmineApi) -> Unit,
    ) {
        try {
            request(
                buildApi(
                    host = host,
                    useXml = useXml
                )
            )
        } catch (hostException: UnknownHostException) {
            throw Exception("Хост не найден")
        } catch (nullException: NullPointerException) {
            throw Exception("Тело ответа пустое")
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    @Throws(Exception::class)
    private suspend fun <T> handleResponse(
        host: String,
        request: suspend (RedmineApi) -> Response<T>,
    ): T {
        try {
            val response = request(buildApi(host))

            if (response.code() == 401) {
                throw Exception("Неверный API-ключ")
            }

            if (!response.isSuccessful) {
                throw Exception("Код ошибки: ${response.code()}")
            }

            return response.body() ?: throw NullPointerException("")
        } catch (hostException: UnknownHostException) {
            throw Exception("Хост не найден")
        } catch (nullException: NullPointerException) {
            throw Exception("Тело ответа пустое")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    private fun buildApi(
        host: String,
        useXml: Boolean = false,
    ): RedmineApi {
        val converterFactory: Converter.Factory = if (useXml) {
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .build()
            )
        } else {
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
                    .registerTypeAdapter(Date::class.java, DateDeserializer())
                    .create()
            )
        }

        val retrofit = Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl("https://$host")
            .build()

        return retrofit.create(RedmineApi::class.java)
    }
}