package me.kofesst.android.redminecomposeapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.kofesst.android.redminecomposeapp.feature.data.repository.RedmineRepositoryImpl
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
    fun provideUseCases(repository: RedmineRepository): UseCases {
        return UseCases(
            getCurrentUser = GetCurrentUser(repository),
            getProjects = GetProjects(repository),
            getIssues = GetIssues(repository),
            getIssueDetails = GetIssueDetails(repository),
            getTrackers = GetTrackers(repository),
            getStatuses = GetStatuses(repository),
            getMembers = GetMembers(repository),
            validateForEmptyField = ValidateForEmptyField(),
            validateForNotNullField = ValidateForNotNullField()
        )
    }
}