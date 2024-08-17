package com.smh.movie.data.helper

import arrow.core.Either
import arrow.core.left
import kotlinx.serialization.SerializationException

const val ERROR_TITLE_GENERAL = "Error"
const val ERROR_MESSAGE_GENERAL = "Something went wrong. Please try again."
const val ERROR_JSON_CONVERSION = "Error json conversion. Please try again."

internal fun Exception.convertEither(): Either<DataException, Nothing> = when (this) {
    is SerializationException -> {
        DataException.Api(
            message = this.message ?: ERROR_JSON_CONVERSION,
            title = ERROR_TITLE_GENERAL,
            errorCode = -1
        ).left()
    }
    else -> {
        DataException.Api(
            message = this.message ?: ERROR_MESSAGE_GENERAL,
            title = ERROR_TITLE_GENERAL,
            errorCode = -1
        ).left()
    }
}