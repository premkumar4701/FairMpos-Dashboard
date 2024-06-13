package api.fairdashboard

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class FairDashboardService constructor(private val httpClient: HttpClient) : ServiceRepository() {

    suspend fun getFairsBy(status: Int): Flow<ResultState<FairDashboardResponse>> {
        return flowOf(
            safeApiCall {
                val response = httpClient.get(urlString = "api/dashboard/fairs") {
                    url {
                        parameters.append("status", status.toString())
                    }
                }.body<FairDashboardResponse>()
                response
            }
        )
    }
}