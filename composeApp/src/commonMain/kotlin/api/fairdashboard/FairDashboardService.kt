package api.fairdashboard

import api.bestsellers.BestSellersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class FairDashboardService constructor(private val httpClient: HttpClient) {
  suspend fun getFairsBy(status: Int): Flow<Result> {
    return flow {
      try {
        val response: HttpResponse =
            httpClient.get(urlString = "api/dashboard/fairs") {
              url { parameters.append("status", status.toString()) }
            }
        when (val statusCode = response.status) {
          HttpStatusCode.OK -> {
            val bestSellersResponse: BestSellersResponse = response.body()
            if (bestSellersResponse.success) {
              emit(Result.Success(bestSellersResponse, statusCode))
            } else {
              emit(Result.Failure(null, bestSellersResponse.message))
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
