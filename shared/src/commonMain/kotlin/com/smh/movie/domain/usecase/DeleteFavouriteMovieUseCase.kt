package com.smh.movie.domain.usecase

import com.smh.movie.domain.repository.MovieRepository
import com.smh.movie.utility.Dispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteFavouriteMovieUseCase: KoinComponent {

    private val repository: MovieRepository by inject()
    private val dispatcher: Dispatcher by inject()

    suspend operator fun invoke(id: Long): Unit = withContext(dispatcher.io) {
        repository.deleteFavouriteMovie(id)
    }
}