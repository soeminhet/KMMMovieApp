package com.smh.movie.data.remote.service

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

internal class MovieService: KtorApi() {
    suspend fun getPopularMovies(page: Int): HttpResponse {
        return client.get {
            pathUrl("movie/popular")
            parameter("page", page)
        }
    }

    suspend fun getNowPlayingMovies(page: Int): HttpResponse {
        return client.get {
            pathUrl("movie/now_playing")
            parameter("page", page)
        }
    }

    suspend fun getTopRatedMovies(page: Int): HttpResponse {
        return client.get {
            pathUrl("movie/top_rated")
            parameter("page", page)
        }
    }

    suspend fun getUpcomingMovies(page: Int): HttpResponse {
        return client.get {
            pathUrl("movie/upcoming")
            parameter("page", page)
        }
    }
}