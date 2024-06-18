package api.datewiseoverview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class DateWiseOverviewService(private val httpClient: HttpClient) {

  suspend fun getDateWiseOverview(fairId: Long): Flow<Result<DateWiseOverviewResponse>> {
    return flow {
      try {
        val response: HttpResponse =
            httpClient.get(
                urlString = "api/dashboard/fairs-overview/$fairId/date-wise-overview")
        when (val statusCode = response.status) {
          HttpStatusCode.OK -> {
            val dateWiseOverviewResponse: DateWiseOverviewResponse = response.body()
            if (dateWiseOverviewResponse.success) {
              emit(Result.Success(dateWiseOverviewResponse, statusCode))
            } else {
              emit(Result.Failure(null, dateWiseOverviewResponse.message))
            }
          }
          HttpStatusCode.Unauthorized -> {
            emit(Result.Unauthorized(statusCode, "Unauthorized"))
          }
        }
      } catch (e: Exception) {
        emit(Result.Failure(exception = e))
      }
    }
  }
}
