package com.example.flexxo.di

import com.example.flexxo.data.remote.api.services.MoviesService
import com.example.flexxo.data.remote.sources.RemoteDataSourceImpl
import com.example.flexxo.domain.sources.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourcesModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        moviesService: MoviesService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(moviesService)
    }

}