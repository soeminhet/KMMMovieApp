package com.smh.movie.domain.repository

import arrow.core.Either
import com.smh.movie.data.helper.DataException
import com.smh.movie.domain.model.FavouriteMovieModel
import com.smh.movie.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getUpcomingMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun getNowPlayingMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun getTopRatedMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun getPopularMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun insertOrUpdateFavouriteMovie(movie: FavouriteMovieModel)

    fun getAllFavourites(): Flow<List<FavouriteMovieModel>>

    suspend fun deleteFavouriteMovie(id: Long)
}