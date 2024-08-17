package com.smh.movie.android.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smh.movie.android.data.mapper.toFavouriteMovieModel
import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.android.utility.observeMovies
import com.smh.movie.domain.model.FavouriteMovieModel
import com.smh.movie.domain.model.MovieModel
import com.smh.movie.domain.usecase.DeleteFavouriteMovieUseCase
import com.smh.movie.domain.usecase.GetAllFavouriteMoviesUseCase
import com.smh.movie.domain.usecase.GetPopularMoviesUseCase
import com.smh.movie.domain.usecase.GetTopRatedMoviesUseCase
import com.smh.movie.domain.usecase.InsertOrUpdateFavouriteMovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val insertOrUpdateFavouriteMovieUseCase: InsertOrUpdateFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState = _uiState.asStateFlow()

    private val favouriteMoviesState = MutableStateFlow<List<FavouriteMovieModel>>(emptyList())
    private val moviesState = MutableStateFlow<List<MovieModel>>(emptyList())

    private var currentMoviePage = 1
    private var endOfPageReached = false
    private var category: CategoryType = CategoryType.POPULAR

    init {
        loadFavouriteMovies()
        observeMovieUiModels()
    }

    fun fetchMovies(
        type: CategoryType
    ) {
        when(type) {
            CategoryType.POPULAR -> fetchPopularMovies()
            CategoryType.TOP_RATED -> fetchTopRatedMovies()
        }
    }

    fun fetchMore() {
        if (!endOfPageReached) {
            currentMoviePage++
            fetchMovies(category)
        }
    }

    private fun observeMovieUiModels() {
        viewModelScope.launch(Dispatchers.IO) {
            observeMovies(
                favouriteMoviesFlow = favouriteMoviesState,
                moviesFlow = moviesState,
                updateUiState = { movies ->
                    _uiState.update {
                        it.copy(movies = movies)

                    }
                }
            )
        }
    }

    private fun loadFavouriteMovies() {
        viewModelScope.launch {
            getAllFavouriteMoviesUseCase()
                .distinctUntilChanged()
                .collectLatest { movies ->
                    favouriteMoviesState.value = movies
                }
        }
    }

    private fun fetchPopularMovies() {
        if (currentMoviePage == 1) setLoading(true) else setMoreLoading(true)
        viewModelScope.launch {
            getPopularMoviesUseCase(currentMoviePage)
                .onRight { movies ->
                    setLoading(false)
                    setMoreLoading(false)
                    endOfPageReached = movies.isEmpty()
                    moviesState.update {
                        if (currentMoviePage == 1) movies else (it + movies).distinctBy { it.id }
                    }
                }
                .onLeft { error ->
                    setLoading(false)
                    setMoreLoading(false)
                    Log.e("CategoryViewModel", "fetchPopularMovies: $error")
                }
        }
    }

    private fun fetchTopRatedMovies() {
        if (currentMoviePage == 1) setLoading(true) else setMoreLoading(true)
        viewModelScope.launch {
            getTopRatedMoviesUseCase(currentMoviePage)
                .onRight { movies ->
                    setLoading(false)
                    setMoreLoading(false)
                    endOfPageReached = movies.isEmpty()
                    moviesState.update {
                        if (currentMoviePage == 1) movies else (it + movies).distinctBy { it.id }
                    }
                }
                .onLeft { error ->
                    setLoading(false)
                    setMoreLoading(false)
                    Log.e("CategoryViewModel", "fetchTopRatedMovies: $error")
                }
        }
    }

    fun toggleFavourite(movie: MovieUiModel) {
        viewModelScope.launch {
            if (movie.isFavourite) {
                deleteFavouriteMovieUseCase(movie.id.toLong())
            } else {
                insertOrUpdateFavouriteMovieUseCase(movie.toFavouriteMovieModel())
            }
        }
    }

    private fun setLoading(value: Boolean) {
        _uiState.update {
            it.copy(loading = value)
        }
    }

    private fun setMoreLoading(value: Boolean) {
        _uiState.update {
            it.copy(loadingMore = value)
        }
    }
}

data class CategoryUiState(
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val movies: List<MovieUiModel> = emptyList(),
)