package utils

import api.login.model.LoginResponse
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.LocalDate

fun List<String>.getMessage(): String {
  return if (isNotEmpty()) {
    joinToString(separator = ".")
  } else {
    "API Error"
  }
}

fun LocalDate.getUIDate(): String {
  val day = dayOfMonth
  val daySuffix = getDayOfMonthSuffix(day)
  val month = month
  val year = year
  return "$day$daySuffix $month, $year"
}

fun getDayOfMonthSuffix(n: Int): String {
  if (n in 11..13) {
    return "th"
  }
  return when (n % 10) {
    1 -> "st"
    2 -> "nd"
    3 -> "rd"
    else -> "th"
  }
}

sealed class Result<out R> {
  data class Success<out R>(val value: R, val statusCode: HttpStatusCode) : Result<R>()

  data class Unauthorized(val statusCode: HttpStatusCode, val message: String) :  Result<Nothing>()

  data class Failure(val exception: Exception? = null, val message: List<String>? = null) : Result<Nothing>()
}
