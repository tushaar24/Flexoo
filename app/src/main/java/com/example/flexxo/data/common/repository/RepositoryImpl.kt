package com.example.flexxo.data.common.repository

import com.example.flexxo.data.common.models.MovieCreditsEntity
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.domain.sources.remote.RemoteDataSource
import com.example.flexxo.utils.NetworkResult
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getTopRatedMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies> {
        return remoteDataSource.getTopRatedMovies(
            apiKey,
            page
        )
    }

    override suspend fun getUpComingMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies> {
        return remoteDataSource.getUpComingMovies(
            apiKey,
            page
        )
    }

    override suspend fun getPopularMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies> {
        return remoteDataSource.getPopularMovies(
            apiKey,
            page
        )
    }

    override suspend fun searchMovies(
        apiKey: String,
        movieQuery: String
    ): NetworkResult<Movies> {
        return remoteDataSource.searchMovies(
            apiKey,
            movieQuery
        )
    }

    override suspend fun getMovieCredits(
        movieId: Int,
        apiKey: String
    ): NetworkResult<MovieCreditsEntity> {
        return remoteDataSource.getMovieCredits(
            movieId,
            apiKey
        )
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        apiKey: String
    ): NetworkResult<MovieDetails> {
        return remoteDataSource.getMovieDetails(
            movieId,
            apiKey
        )
    }

    override suspend fun getSimilarMovies(movieId: Int, apiKey: String): NetworkResult<Movies> {
        return remoteDataSource.getSimilarMovies(
            movieId,
            apiKey
        )
    }

    override suspend fun getRecommendedMovies(movieId: Int, apiKey: String): NetworkResult<Movies> {
        return remoteDataSource.getRecommendedMovies(
            movieId,
            apiKey
        )
    }
}