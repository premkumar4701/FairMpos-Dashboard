package api.billwiseoverview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class BillWiseOverviewService(private val httpClient: HttpClient) {
  suspend fun getBillWiseOverview(fairId: Long): Flow<Result<BillWiseOverviewResponse>> {
    return flow {
      try {
        val response: HttpResponse =
            httpClient.get(
                urlString = "api/dashboard/fairs-overview/$fairId/bill-wise-overview")
        when (val statusCode = response.status) {
          HttpStatusCode.OK -> {
            val bestSellersResponse: BillWiseOverviewResponse = response.body()
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
