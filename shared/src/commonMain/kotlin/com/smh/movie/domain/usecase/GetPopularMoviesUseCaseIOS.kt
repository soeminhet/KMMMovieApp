package com.smh.movie.domain.usecase

import com.smh.movie.domain.model.MovieModel
import com.smh.movie.domain.repository.MovieRepository
import com.smh.movie.utility.Dispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPopularMoviesUseCaseIOS: KoinComponent {

    private val repository: MovieRepository by inject()
    private val dispatcher: Dispatcher by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page: Int): List<MovieModel> = withContext(dispatcher.io) {
        val movies = repository.getPopularMovies(page)
        movies.onLeft { throw Exception(message = it.message ?: "Something went wrong") }
        movies.getOrNull() ?: emptyList()
    }
}