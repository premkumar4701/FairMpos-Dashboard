package utils

import api.login.model.LoginResponse
import io.ktor.http.HttpStatusCode

fun List<String>.getMessage(): String {
    return if (isNotEmpty()) {
        joinToString(separator = ".")
    } else {
        "API Error"
    }
}

sealed class Result {
    data class Success(val response: LoginResponse, val statusCode: HttpStatusCode) : Result()
    data class Unauthorized(val statusCode: HttpStatusCode, val message: String) : Result()
    data class Failure(val statusCode: HttpStatusCode, val message: List<String>) : Result()
}