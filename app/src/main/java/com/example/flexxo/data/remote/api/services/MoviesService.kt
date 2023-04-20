package com.example.flexxo.data.remote.api.services

import com.example.flexxo.data.common.models.MovieCreditsEntity
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.utils.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("/3/movie/upcoming")
    suspend fun getUpComingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): NetworkResult<Movies>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): NetworkResult<Movies>

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): NetworkResult<Movies>

    @GET("/3/search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): NetworkResult<Movies>

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): NetworkResult<MovieCreditsEntity>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): NetworkResult<MovieDetails>

    @GET("/3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): NetworkResult<Movies>

    @GET("/3/movie/{movie_id}/recommendations")
    suspend fun getRecommendedMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): NetworkResult<Movies>

}