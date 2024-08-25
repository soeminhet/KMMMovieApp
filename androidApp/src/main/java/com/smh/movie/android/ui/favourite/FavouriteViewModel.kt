package com.smh.movie.android.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smh.movie.domain.model.MovieUiModel
import com.smh.movie.domain.usecase.DeleteFavouriteMovieUseCase
import com.smh.movie.domain.usecase.GetAllFavouriteMoviesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouriteViewModel(
    getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
) : ViewModel() {
    val favouriteMovies = getAllFavouriteMoviesUseCase()
        .map {
            it.map { movie ->
                MovieUiModel(
                    id = movie.id!!.toInt(),
                    title = movie.title,
                    description = movie.description,
                    imageUrl = movie.imageUrl,
                    isFavourite = true,
                    releaseDate = movie.releaseDate,
                    votePercentage = movie.votePercentage,
                    backdropUrl = movie.backdropUrl
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavourite(movie: MovieUiModel) {
        viewModelScope.launch {
            deleteFavouriteMovieUseCase(movie.id.toLong())
        }
    }
}