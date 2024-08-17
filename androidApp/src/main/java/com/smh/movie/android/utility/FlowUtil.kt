package com.smh.movie.android.utility

import com.smh.movie.android.data.mapper.toUiModel
import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.domain.model.FavouriteMovieModel
import com.smh.movie.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

suspend fun observeMovies(
    favouriteMoviesFlow: Flow<List<FavouriteMovieModel>>,
    moviesFlow: Flow<List<MovieModel>>,
    updateUiState: (List<MovieUiModel>) -> Unit
) {
    favouriteMoviesFlow
        .distinctUntilChanged()
        .combine(moviesFlow) { favouriteMovies, movies ->
            movies.map { movie ->
                val isFavourite = favouriteMovies.any { it.id?.toInt() == movie.id }
                movie.toUiModel(isFavourite)
            }
        }
        .collectLatest { movies ->
            updateUiState(movies)
        }
}