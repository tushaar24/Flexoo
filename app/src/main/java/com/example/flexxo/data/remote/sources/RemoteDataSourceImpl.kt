package com.example.flexxo.data.remote.sources

import com.example.flexxo.data.common.models.MovieCreditsEntity
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.data.common.models.MovieVideos
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.data.remote.api.services.MoviesService
import com.example.flexxo.domain.sources.remote.RemoteDataSource
import com.example.flexxo.utils.NetworkResult
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val moviesService: MoviesService
) : RemoteDataSource {

    override suspend fun getTopRatedMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies> {
        return moviesService.getTopRatedMovies(
            apiKey,
            page
        )
    }

    override suspend fun getUpComingMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies> {
        return moviesService.getUpComingMovies(
            apiKey,
            page
        )
    }

    override suspend fun getPopularMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies> {
        return moviesService.getPopularMovies(
            apiKey,
            page
        )
    }

    override suspend fun searchMovies(
        apiKey: String,
        movieQuery: String
    ): NetworkResult<Movies> {
        return moviesService.searchMovie(
            apiKey,
            movieQuery
        )
    }

    override suspend fun getMovieCredits(
        movieId: Int,
        apiKey: String
    ): NetworkResult<MovieCreditsEntity> {
        return moviesService.getMovieCredits(
            movieId,
            apiKey
        )
    }

    override suspend fun getMovieDetails(
        movieId: Int,
        apiKey: String,
    ): NetworkResult<MovieDetails> {
        return moviesService.getMovieDetails(
            movieId,
            apiKey
        )
    }

    override suspend fun getSimilarMovies(movieId: Int, apiKey: String): NetworkResult<Movies> {
        return moviesService.getSimilarMovies(
            movieId,
            apiKey
        )
    }

    override suspend fun getRecommendedMovies(movieId: Int, apiKey: String): NetworkResult<Movies> {
        return moviesService.getRecommendedMovies(
            movieId,
            apiKey
        )
    }

    override suspend fun getMovieVideos(movieId: Int, apiKey: String): NetworkResult<MovieVideos> {
        return moviesService.getMovieVideos(
            movieId,
            apiKey
        )
    }
}