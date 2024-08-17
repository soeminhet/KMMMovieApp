package com.smh.movie.data.mapper

import com.smh.movie.domain.model.FavouriteMovieModel
import comsmhmoviedatabase.FavouriteMovieEntity

fun FavouriteMovieEntity.toModel(): FavouriteMovieModel {
    return FavouriteMovieModel(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        releaseDate = releaseDate,
        backdropUrl = backdropUrl,
        votePercentage = votePercentage.toInt()
    )
}