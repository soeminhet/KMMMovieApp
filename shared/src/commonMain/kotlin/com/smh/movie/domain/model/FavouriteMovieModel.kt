package com.smh.movie.domain.model

data class FavouriteMovieModel(
    val id: Long?,
    val title: String,
    val description: String,
    val imageUrl: String,
    val releaseDate: String,
    val backdropUrl: String,
    val votePercentage: Int
)
