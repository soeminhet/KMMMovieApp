package com.smh.movie.android.data.mapper

import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.domain.model.FavouriteMovieModel
import com.smh.movie.domain.model.MovieModel

fun MovieUiModel.toFavouriteMovieModel() = FavouriteMovieModel(
    id = id.toLong(),
    title = title,
    description = description,
    imageUrl = imageUrl,
    releaseDate = releaseDate,
    backdropUrl = backdropUrl,
    votePercentage = votePercentage
)

fun MovieModel.toUiModel(isFavourite: Boolean): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        releaseDate = releaseDate,
        backdropUrl = backdropUrl,
        votePercentage = votePercentage,
        isFavourite = isFavourite
    )
}