package me.kofesst.android.redminecomposeapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.kofesst.android.redminecomposeapp.feature.data.repository.RedmineRepositoryImpl
import me.kofesst.android.redminecomposeapp.feature.data.storage.AppDatabase
import me.kofesst.android.redminecomposeapp.feature.domain.repository.RedmineRepository
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.*
import me.kofesst.android.redminecomposeapp.feature.domain.util.UserHolder
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
    fun provideRepository(userHolder: UserHolder): RedmineRepository {
        return RedmineRepositoryImpl(userHolder)
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
    fun provideUseCases(
        repository: RedmineRepository,
        database: AppDatabase,
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
            getAccount = GetAccount(database),
            getAccounts = GetAccounts(database),
            addAccount = AddAccount(database),
            updateAccount = UpdateAccount(database),
            deleteAccount = DeleteAccount(database)
        )
    }
}