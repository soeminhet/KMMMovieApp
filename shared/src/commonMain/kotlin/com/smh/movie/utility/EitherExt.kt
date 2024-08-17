package com.smh.movie.utility

import arrow.core.Either

fun <L, R> Either<L, R>.toResult(): Result<R> {
    return fold(
        { Result.failure(Exception(it.toString())) },
        { Result.success(it) }
    )
}