package com.smh.movie.data.helper

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

internal suspend inline fun <reified Response, Success> handleHttpResponse(
    serviceCall: () -> HttpResponse,
    mapper: (Response) -> Success,
): Either<DataException, Success> {
    return try {
        val response = serviceCall()
        val statusCode = response.status.value
        val body = response.body<Response>()
        if (statusCode in 200..299) {
            mapper(body).right()
        } else {
            DataException.Api(
                errorCode = statusCode,
                message = body.toString()
            ).left()
        }
    } catch (e: Exception) {
        e.convertEither()
    }
}