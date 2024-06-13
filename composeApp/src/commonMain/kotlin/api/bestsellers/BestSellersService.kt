package api.bestsellers

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class BestSellersService constructor(private val httpClient: HttpClient) : ServiceRepository() {

    suspend fun getBestSellers(
        fairId: Long,
        sortBy: Int,
        noOfItems: Int
    ): Flow<ResultState<BestSellersResponse>> {
        return flowOf(
            safeApiCall {
                val response =
                    httpClient.get(urlString = "/api/dashboard/fairs/{fairid}/bestsellers") {
                        url {
                            path("api", "dashboard", "fairs", fairId.toString(), "bestsellers")
                            parameters.append("sortby", sortBy.toString())
                            parameters.append("noOfItems", noOfItems.toString())
                        }
                    }.body<BestSellersResponse>()
                response
            }
        )
    }
}