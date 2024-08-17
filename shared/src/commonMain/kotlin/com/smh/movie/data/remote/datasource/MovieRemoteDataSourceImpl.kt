package com.smh.movie.data.remote.datasource

import arrow.core.Either
import com.smh.movie.data.helper.DataException
import com.smh.movie.data.helper.handleHttpResponse
import com.smh.movie.data.mapper.toModel
import com.smh.movie.data.remote.response.MoviesResponse
import com.smh.movie.data.remote.service.MovieService
import com.smh.movie.domain.datasource.MovieRemoteDataSource
import com.smh.movie.domain.model.MovieModel

internal class MovieRemoteDataSourceImpl(
    private val movieService: MovieService
) : MovieRemoteDataSource {
    override suspend fun getUpcomingMovies(page: Int): Either<DataException, List<MovieModel>> {
        return handleHttpResponse<MoviesResponse, List<MovieModel>>(
            serviceCall = { movieService.getUpcomingMovies(page) },
            mapper = { it.results.map { movieResponse -> movieResponse.toModel() } }
        )
    }

    override suspend fun getNowPlayingMovies(page: Int): Either<DataException, List<MovieModel>> {
        return handleHttpResponse<MoviesResponse, List<MovieModel>>(
            serviceCall = { movieService.getNowPlayingMovies(page) },
            mapper = { it.results.map { movieResponse -> movieResponse.toModel() } }
        )
    }

    override suspend fun getTopRatedMovies(page: Int): Either<DataException, List<MovieModel>> {
        return handleHttpResponse<MoviesResponse, List<MovieModel>>(
            serviceCall = { movieService.getTopRatedMovies(page) },
            mapper = { it.results.map { movieResponse -> movieResponse.toModel() } }
        )
    }

    override suspend fun getPopularMovies(page: Int): Either<DataException, List<MovieModel>> {
        return handleHttpResponse<MoviesResponse, List<MovieModel>>(
            serviceCall = { movieService.getPopularMovies(page) },
            mapper = { it.results.map { movieResponse -> movieResponse.toModel() } }
        )
    }
}

