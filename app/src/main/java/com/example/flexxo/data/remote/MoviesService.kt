package com.example.flexxo.data.remote

import com.example.flexxo.data.models.MovieDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {
    @GET("/3/movie/upcoming")
    suspend fun getUpComingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): List<MovieDetails>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): List<MovieDetails>

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): List<MovieDetails>

    @GET("/3/search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): List<MovieDetails>

    @GET("/3/movie/latest")
    suspend fun getLatestMovies(
        @Query("api_key") apiKey: String
    ): List<MovieDetails>

    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String
    ): List<MovieDetails>
}