package com.smh.movie.domain.datasource

import com.smh.movie.domain.model.FavouriteMovieModel
import kotlinx.coroutines.flow.Flow

interface FavouriteMovieDataSource {
    suspend fun insertFavouriteMovie(movie: FavouriteMovieModel)

    fun getAllFavourites(): Flow<List<FavouriteMovieModel>>

    suspend fun deleteFavouriteMovie(id: Long)
}