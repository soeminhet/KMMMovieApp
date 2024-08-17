package com.smh.movie.domain.usecase

import com.smh.movie.domain.repository.MovieRepository
import com.smh.movie.utility.Dispatcher
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAllFavouriteMoviesUseCase: KoinComponent {

    private val repository: MovieRepository by inject()
    private val dispatcher: Dispatcher by inject()

    operator fun invoke() = repository.getAllFavourites().flowOn(dispatcher.io)
}