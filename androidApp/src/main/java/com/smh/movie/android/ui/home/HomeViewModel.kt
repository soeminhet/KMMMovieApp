package com.smh.movie.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smh.movie.android.data.mapper.toFavouriteMovieModel
import com.smh.movie.android.data.mapper.toUiModel
import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.android.utility.observeMovies
import com.smh.movie.domain.model.FavouriteMovieModel
import com.smh.movie.domain.model.MovieModel
import com.smh.movie.domain.usecase.DeleteFavouriteMovieUseCase
import com.smh.movie.domain.usecase.GetAllFavouriteMoviesUseCase
import com.smh.movie.domain.usecase.GetNowPlayingMoviesUseCase
import com.smh.movie.domain.usecase.GetPopularMoviesUseCase
import com.smh.movie.domain.usecase.GetTopRatedMoviesUseCase
import com.smh.movie.domain.usecase.GetUpcomingMoviesUseCase
import com.smh.movie.domain.usecase.InsertOrUpdateFavouriteMovieUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getAllFavouriteMoviesUseCase: GetAllFavouriteMoviesUseCase,
    private val insertOrUpdateFavouriteMovieUseCase: InsertOrUpdateFavouriteMovieUseCase,
    private val deleteFavouriteMovieUseCase: DeleteFavouriteMovieUseCase
): ViewModel() {

    private val viewModelDataState = MutableStateFlow(ViewModelDataState())

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var currentUpcomingMoviePage = 1
    private var endOfPageReached = false

    init {
        loadFavouriteMovies()
        fetchNowPlayingMovies()
        fetchUpcomingMovies()
        fetchPopularMovies()
        fetchTopRatedMovies()
        observeViewModelDataState()
    }

    private fun observeViewModelDataState() {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                observeMovies(
                    favouriteMoviesFlow = viewModelDataState.map { it.favouriteMovies },
                    moviesFlow = viewModelDataState.map { it.upcomingMovies },
                    updateUiState = { movies ->
                        _uiState.update {
                            it.copy(
                                upcomingMovies = movies
                            )
                        }
                    }
                )
            }

            launch {
                observeMovies(
                    favouriteMoviesFlow = viewModelDataState.map { it.favouriteMovies },
                    moviesFlow = viewModelDataState.map { it.nowPlayingMovies },
                    updateUiState = { movies ->
                        _uiState.update {
                            it.copy(
                                nowPlayingMovies = movies
                            )
                        }
                    }
                )
            }

            launch {
                observeMovies(
                    favouriteMoviesFlow = viewModelDataState.map { it.favouriteMovies },
                    moviesFlow = viewModelDataState.map { it.popularMovies },
                    updateUiState = { movies ->
                        _uiState.update {
                            it.copy(
                                popularMovies = movies
                            )
                        }
                    }
                )
            }

            launch {
                observeMovies(
                    favouriteMoviesFlow = viewModelDataState.map { it.favouriteMovies },
                    moviesFlow = viewModelDataState.map { it.topRatedMovies },
                    updateUiState = { movies ->
                        _uiState.update {
                            it.copy(
                                topRatedMovies = movies
                            )
                        }
                    }
                )
            }

            launch {
                viewModelDataState
                    .map { it.isLoadingPopularMovies || it.isLoadingTopRatedMovies || it.isLoadingNowPlayingMovies }
                    .distinctUntilChanged()
                    .collectLatest { loading ->
                        _uiState.update {
                            it.copy(isLoading = loading)
                        }
                    }
            }
        }
    }

    private fun loadFavouriteMovies() {
        viewModelScope.launch {
            getAllFavouriteMoviesUseCase()
                .collectLatest { movies ->
                    Log.e("HomeViewModel", "load: $movies")
                    viewModelDataState.update {
                        it.copy(
                            favouriteMovies = movies
                        )
                    }
                }
        }
    }

    private fun fetchNowPlayingMovies() {
        setNowPlayingLoading(true)
        viewModelScope.launch {
            getNowPlayingMoviesUseCase(1)
                .onRight { movies ->
                    setNowPlayingLoading(false)
                    viewModelDataState.update {
                        it.copy(
                            nowPlayingMovies = movies.take(5)
                        )
                    }
                }
                .onLeft {
                    setNowPlayingLoading(false)
                    Log.e("HomeViewModel", "fetchNowPlayingMovies: $it")
                }
        }
    }

    private fun fetchPopularMovies() {
        setPopularLoading(true)
        viewModelScope.launch {
            getPopularMoviesUseCase(1)
                .onRight { movies ->
                    setPopularLoading(false)
                    viewModelDataState.update {
                        it.copy(
                            popularMovies = movies.take(5)
                        )
                    }
                }
                .onLeft {
                    setPopularLoading(false)
                    Log.e("HomeViewModel", "fetchPopularMovies: $it")
                }
        }
    }

    private fun fetchTopRatedMovies() {
        setTopRatedLoading(true)
        viewModelScope.launch {
            getTopRatedMoviesUseCase(1)
                .onRight { movies ->
                    setTopRatedLoading(false)
                    viewModelDataState.update {
                        it.copy(
                            topRatedMovies = movies.take(5)
                        )
                    }
                }
                .onLeft {
                    setTopRatedLoading(false)
                    Log.e("HomeViewModel", "fetchTopRatedMovies: $it")
                }
        }
    }

    private fun fetchUpcomingMovies() {
        setMoreUpcomingLoading(currentUpcomingMoviePage > 1)
        viewModelScope.launch {
            getUpcomingMoviesUseCase(currentUpcomingMoviePage)
                .onRight { movies ->
                    endOfPageReached = movies.isEmpty()
                    setMoreUpcomingLoading(false)
                    viewModelDataState.update {
                        it.copy(
                            upcomingMovies = if (currentUpcomingMoviePage == 1) movies else (it.upcomingMovies + movies).distinctBy { it.id },
                        )
                    }
                }
                .onLeft { error ->
                    setMoreUpcomingLoading(false)
                    Log.e("HomeViewModel", "fetchUpcomingMovies: $error")
                }
        }
    }

    fun fetchMoreUpcomingMovies() {
        if (!endOfPageReached) {
            currentUpcomingMoviePage++
            fetchUpcomingMovies()
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

    private fun setNowPlayingLoading(
        isLoadingNowPlayingMovies: Boolean
    ) {
        viewModelDataState.update {
            it.setLoadingNowPlayingMovies(isLoadingNowPlayingMovies)
        }
    }

    private fun setPopularLoading(
        isLoadingPopularMovies: Boolean
    ) {
        viewModelDataState.update {
            it.setLoadingPopularMovies(isLoadingPopularMovies)
        }
    }

    private fun setTopRatedLoading(
        isLoadingTopRatedMovies: Boolean
    ) {
        viewModelDataState.update {
            it.setLoadingTopRatedMovies(isLoadingTopRatedMovies)
        }
    }

    private fun setMoreUpcomingLoading(
        isMoreLoadingUpcomingMovies: Boolean
    ) {
        _uiState.update {
            it.setLoadingUpcomingMovies(isMoreLoadingUpcomingMovies)
        }
    }
}

private data class ViewModelDataState(
    val isLoadingNowPlayingMovies: Boolean = false,
    val isLoadingPopularMovies: Boolean = false,
    val isLoadingTopRatedMovies: Boolean = false,
    val upcomingMovies: List<MovieModel> = emptyList(),
    val nowPlayingMovies: List<MovieModel> = emptyList(),
    val popularMovies: List<MovieModel> = emptyList(),
    val topRatedMovies: List<MovieModel> = emptyList(),
    val favouriteMovies: List<FavouriteMovieModel> = emptyList()
) {
    fun setLoadingNowPlayingMovies(isLoading: Boolean) = copy(isLoadingNowPlayingMovies = isLoading)

    fun setLoadingPopularMovies(isLoading: Boolean) = copy(isLoadingPopularMovies = isLoading)

    fun setLoadingTopRatedMovies(isLoading: Boolean) = copy(isLoadingTopRatedMovies = isLoading)
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val isMoreLoadingUpcomingMovies: Boolean = false,
    val upcomingMovies: List<MovieUiModel> = emptyList(),
    val nowPlayingMovies: List<MovieUiModel> = emptyList(),
    val popularMovies: List<MovieUiModel> = emptyList(),
    val topRatedMovies: List<MovieUiModel> = emptyList(),
) {
    fun setLoadingUpcomingMovies(isLoading: Boolean) = copy(isMoreLoadingUpcomingMovies = isLoading)
}