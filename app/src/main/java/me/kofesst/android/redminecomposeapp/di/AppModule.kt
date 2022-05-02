package me.kofesst.android.redminecomposeapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.UseCases
import me.kofesst.android.redminecomposeapp.feature.domain.usecase.ValidateForEmptyField
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUseCases(): UseCases {
        return UseCases(
            validateForEmptyField = ValidateForEmptyField()
        )
    }
}