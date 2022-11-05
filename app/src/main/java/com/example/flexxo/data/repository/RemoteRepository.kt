package com.example.flexxo.data.repository

import com.example.flexxo.data.models.Movies
import com.example.flexxo.utils.Communicator

class RemoteRepository {

    private val movieService = Communicator.getMovieService()

    suspend fun getTopRatedMovies(api_key: String, page: Int): Movies {
        return movieService.getTopRatedMovies(api_key, page)
    }

    suspend fun getUpComingMovies(api_key: String, page: Int): Movies {
        return movieService.getUpComingMovies(api_key, page)
    }

    suspend fun getPopularMovies(api_key: String, page: Int): Movies {
        return movieService.getPopularMovies(api_key, page)
    }

    suspend fun searchMovies(api_key: String, query: String): Movies {
        return movieService.searchMovie(api_key, query)
    }

    suspend fun getLatestMovies(api_key: String): Movies{
        return movieService.getLatestMovies(api_key)
    }

    suspend fun getNowPlayingMovies(api_key: String): Movies{
        return movieService.getNowPlayingMovies(api_key)
    }
}