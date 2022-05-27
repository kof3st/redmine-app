package me.kofesst.android.redminecomposeapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.kofesst.android.redminecomposeapp.data.repository.RedmineRepositoryImpl
import me.kofesst.android.redminecomposeapp.data.storage.AppDatabase
import me.kofesst.android.redminecomposeapp.domain.repository.RedmineRepository
import me.kofesst.android.redminecomposeapp.domain.usecase.*
import me.kofesst.android.redminecomposeapp.domain.util.UserHolder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserHolder(): UserHolder {
        return UserHolder()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile("app_prefs")
        })
    }

    @Provides
    @Singleton
    fun provideRepository(
        userHolder: UserHolder,
        database: AppDatabase,
        dataStore: DataStore<Preferences>,
    ): RedmineRepository {
        return RedmineRepositoryImpl(userHolder, database, dataStore)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        repository: RedmineRepository,
    ): UseCases {
        return UseCases(
            getCurrentUser = GetCurrentUser(repository),
            getProjects = GetProjects(repository),
            getProjectIssues = GetProjectIssues(repository),
            getOwnedIssues = GetOwnedIssues(repository),
            getAssignedIssues = GetAssignedIssues(repository),
            getIssueDetails = GetIssueDetails(repository),
            createIssue = CreateIssue(repository),
            updateIssue = UpdateIssue(repository),
            getTrackers = GetTrackers(repository),
            getStatuses = GetStatuses(repository),
            getMembers = GetMembers(repository),
            validateForEmptyField = ValidateForEmptyField(),
            validateForNotNullField = ValidateForNotNullField(),
            getAccount = GetAccount(repository),
            getAccounts = GetAccounts(repository),
            addAccount = AddAccount(repository),
            updateAccount = UpdateAccount(repository),
            deleteAccount = DeleteAccount(repository),
            saveSession = SaveSession(repository),
            restoreSession = RestoreSession(repository),
            clearSession = ClearSession(repository)
        )
    }
}