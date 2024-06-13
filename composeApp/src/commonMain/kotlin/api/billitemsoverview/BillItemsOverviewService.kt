package api.billitemsoverview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class BillItemsOverviewService constructor(private val httpClient: HttpClient) {
  suspend fun getBillItemsOverview(billId: Long): Flow<Result<BillItemsOverviewResponse>> {
    return flow {
      try {
        val response: HttpResponse =
            httpClient.get(urlString = "api/dashboard/bill-overview/{bill_id}") {
              url { path("api", "dashboard", "bill-overview", billId.toString()) }
            }
        when (val statusCode = response.status) {
          HttpStatusCode.OK -> {
            val bestSellersResponse: BillItemsOverviewResponse = response.body()
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
