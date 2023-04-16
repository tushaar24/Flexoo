package com.example.flexxo.data.common.models

data class MovieCreditsEntity(
    val cast: List<MovieCastEntity>,
    val crew: List<MovieCrewEntity>,
    val id: Int
)