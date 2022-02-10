package com.example.flexxo.utils

import com.example.flexxo.data.remote.MoviesService
import com.example.flexxo.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Communicator {
    fun getMovieService() : MoviesService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesService::class.java)
    }
}