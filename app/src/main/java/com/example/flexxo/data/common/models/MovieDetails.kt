package com.example.flexxo.data.common.models

import java.io.Serializable

data class MovieDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,

    /*
        Below fields are required when you request details for a particular movie. These fields will have default
        values when getting data from api points like: GET Latest Movies, GET Popular Movies etc.
     */

    val runtime: Int?,
    val status: String?

) : Serializable