package com.smh.movie.domain.datasource

import arrow.core.Either
import com.smh.movie.data.helper.DataException
import com.smh.movie.domain.model.MovieModel

interface MovieRemoteDataSource {
    suspend fun getUpcomingMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun getNowPlayingMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun getTopRatedMovies(page: Int): Either<DataException, List<MovieModel>>

    suspend fun getPopularMovies(page: Int): Either<DataException, List<MovieModel>>
}