package com.smh.movie.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieResponse(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path")
    val posterImage: String?,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("backdrop_path")
    val backdropImage: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
)