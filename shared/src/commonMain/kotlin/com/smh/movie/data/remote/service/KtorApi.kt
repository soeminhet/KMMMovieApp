package com.smh.movie.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://api.themoviedb.org/"
private const val AccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzYzA0Zjc3Mzg1MzRkMjQ4MzM3ZjIxOGM3Njg0MzAwNyIsIm5iZiI6MTcyMzM2NDQyMy45MzQ1NjYsInN1YiI6IjVmNGM1ZTE5MTY4NGY3MDAzNjcyNTNjMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.vDk_7LkNT0OTXlKSKgOoSJN0BQbDVQuKIWjZRov9wr0"

internal abstract class KtorApi {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                }
            )
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = AccessToken,
                        refreshToken = ""
                    )
                }
            }
        }
    }

    fun HttpRequestBuilder.pathUrl(path: String) {
        url {
            takeFrom(urlString = BASE_URL)
            path("3", path)
        }
    }
}