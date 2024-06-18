package api.fairoverview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class FairOverviewService(private val httpClient: HttpClient) {
  suspend fun getFairOverviewById(fairId: Long): Flow<Result<FairOverviewResponse>> {
    return flow {
      try {
        val response: HttpResponse =
            httpClient.get(urlString = "api/dashboard/fairs-overview/$fairId")
        when (val statusCode = response.status) {
          HttpStatusCode.OK -> {
            val fairOverviewResponse: FairOverviewResponse = response.body()
            if (fairOverviewResponse.success) {
              emit(Result.Success(fairOverviewResponse, statusCode))
            } else {
              emit(Result.Failure(null, fairOverviewResponse.message))
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
