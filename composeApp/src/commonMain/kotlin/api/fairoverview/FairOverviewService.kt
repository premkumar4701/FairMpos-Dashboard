package api.fairoverview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class FairOverviewService(private val httpClient: HttpClient): ServiceRepository() {
    suspend fun getFairOverviewById(
        fairId: Long,
    ): Flow<ResultState<FairOverView>> {
        return flowOf(
            safeApiCall {
                val response =
                    httpClient.get(urlString = "api/dashboard/fairs-overview/{fairId}") {
                        url {
                            path("api", "dashboard", "fairs-overview", fairId.toString())
                        }
                    }.body<FairOverView>()
                response
            }
        )
    }
}