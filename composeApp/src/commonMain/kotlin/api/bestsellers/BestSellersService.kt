package api.bestsellers

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class BestSellersService constructor(private val httpClient: HttpClient) {

  suspend fun getBestSellers(fairId: Long, sortBy: Int, noOfItems: Int): Flow<Result<BestSellersResponse>> {
    return flow {
      try {
        val response: HttpResponse =
            httpClient.post("/api/dashboard/fairs/{fairid}/bestsellers") {
              url {
                path("api", "dashboard", "fairs", fairId.toString(), "bestsellers")
                parameters.append("sortby", sortBy.toString())
                parameters.append("noOfItems", noOfItems.toString())
              }
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
