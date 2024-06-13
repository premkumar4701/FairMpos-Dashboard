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

sealed class Result<out R> {
  data class Success<out R>(val value: R, val statusCode: HttpStatusCode) : Result<R>()

  data class Unauthorized(val statusCode: HttpStatusCode, val message: String) :  Result<Nothing>()

  data class Failure(val exception: Exception? = null, val message: List<String>? = null) : Result<Nothing>()
}
