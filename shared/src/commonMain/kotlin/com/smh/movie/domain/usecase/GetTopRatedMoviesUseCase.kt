package com.smh.movie.domain.usecase

import arrow.core.Either
import com.smh.movie.data.helper.DataException
import com.smh.movie.domain.model.MovieModel
import com.smh.movie.domain.repository.MovieRepository
import com.smh.movie.utility.Dispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetTopRatedMoviesUseCase: KoinComponent {

    private val repository: MovieRepository by inject()
    private val dispatcher: Dispatcher by inject()

    suspend operator fun invoke(page: Int): Either<DataException, List<MovieModel>> = withContext(dispatcher.io) {
        repository.getTopRatedMovies(page)
    }
}