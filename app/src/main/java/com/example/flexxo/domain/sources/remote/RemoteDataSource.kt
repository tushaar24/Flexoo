package com.example.flexxo.domain.sources.remote

import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.utils.NetworkResult

interface RemoteDataSource {

    suspend fun getTopRatedMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies>

    suspend fun getUpComingMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies>

    suspend fun getPopularMovies(
        apiKey: String,
        page: Int
    ): NetworkResult<Movies>

    suspend fun searchMovies(
        apiKey: String,
        movieQuery: String
    ): NetworkResult<Movies>


}