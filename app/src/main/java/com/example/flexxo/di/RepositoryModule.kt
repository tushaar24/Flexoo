package com.example.flexxo.di

import com.example.flexxo.data.common.repository.RepositoryImpl
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.domain.sources.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource
    ): Repository {
        return RepositoryImpl(remoteDataSource)
    }

}