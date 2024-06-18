package utils

import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

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

  data class Unauthorized(val statusCode: HttpStatusCode, val message: String) : Result<Nothing>()

  data class Failure(val exception: Exception? = null, val message: List<String>? = null) :
      Result<Nothing>()
}

fun LocalDateTime?.timeAgo(prefix: String = "Updated ", suffix: String = " ago"): String {
  if (this == null) return ""

  val now = Clock.System.now()
  val thisInstant = this.toInstant(timeZone = TimeZone.currentSystemDefault())

  val days = now.minus(thisInstant).inWholeDays
  val hours = now.minus(thisInstant).inWholeHours
  val seconds = now.minus(thisInstant).inWholeSeconds
  val minutes = now.minus(thisInstant).inWholeMinutes

  fun Long.quantity(value: String) = if (this > 1) "${value}s" else value

  var ignoreSuffix = false

  val content =
      when {
        seconds <= 10 -> {
          ignoreSuffix = true
          "just now"
        }
        seconds < 60 -> {
          "few seconds "
        }
        minutes < 60 -> {
          "$minutes ${minutes.quantity("minute")}"
        }
        hours < 24 -> {
          "$hours ${hours.quantity("hour")}"
        }
        days < 7 -> {
          "$days ${days.quantity("day")}"
        }
        else -> {
          when {
            days > 360 -> {
              (days / 360).toString() + " years"
            }
            days > 30 -> {
              (days / 30).toString() + " months"
            }
            else -> {
              (days / 7).toString() + " week"
            }
          }
        }
      }
  return "$prefix$content${if (ignoreSuffix) "" else suffix}"
}
