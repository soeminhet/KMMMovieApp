package com.smh.movie.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
internal data class MoviesResponse(
    val results: List<MovieResponse>
)