package com.smh.movie.data.mapper

import com.smh.movie.data.remote.response.MovieResponse
import com.smh.movie.domain.model.MovieModel
import kotlin.math.roundToInt

internal fun MovieResponse.toModel() = MovieModel(
    id = id,
    title = title,
    description = overview,
    imageUrl = if (posterImage == null) "" else "https://image.tmdb.org/t/p/w500/$posterImage",
    backdropUrl = if (backdropImage == null) "" else "https://image.tmdb.org/t/p/w500/$backdropImage",
    releaseDate = releaseDate,
    votePercentage = voteAverage.times(10).roundToInt()
)