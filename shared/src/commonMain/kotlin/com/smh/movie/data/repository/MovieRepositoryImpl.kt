package com.smh.movie.data.repository

import arrow.core.Either
import com.smh.movie.data.helper.DataException
import com.smh.movie.domain.datasource.FavouriteMovieDataSource
import com.smh.movie.domain.datasource.MovieRemoteDataSource
import com.smh.movie.domain.model.FavouriteMovieModel
import com.smh.movie.domain.model.MovieModel
import com.smh.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource,
    private val favouriteMovieDataSource: FavouriteMovieDataSource
): MovieRepository {
    override suspend fun getUpcomingMovies(page: Int): Either<DataException, List<MovieModel>> {
        return remoteDataSource.getUpcomingMovies(page)
    }

    override suspend fun getNowPlayingMovies(page: Int): Either<DataException, List<MovieModel>> {
        return remoteDataSource.getNowPlayingMovies(page)
    }

    override suspend fun getTopRatedMovies(page: Int): Either<DataException, List<MovieModel>> {
        return remoteDataSource.getTopRatedMovies(page)
    }

    override suspend fun getPopularMovies(page: Int): Either<DataException, List<MovieModel>> {
        return remoteDataSource.getPopularMovies(page)
    }

    override suspend fun insertOrUpdateFavouriteMovie(movie: FavouriteMovieModel) {
        return favouriteMovieDataSource.insertFavouriteMovie(movie)
    }

    override fun getAllFavourites(): Flow<List<FavouriteMovieModel>> {
        return favouriteMovieDataSource.getAllFavourites()
    }

    override suspend fun deleteFavouriteMovie(id: Long) {
        return favouriteMovieDataSource.deleteFavouriteMovie(id)
    }
}