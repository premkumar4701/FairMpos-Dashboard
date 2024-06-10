package api.billwiseoverview

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class BillWiseOverviewService(private val httpClient: HttpClient): ServiceRepository()  {

    suspend fun getBillWiseOverview(
        fairId: Long,
        //TODO date:LocalDate
    ): Flow<ResultState<BillWiseOverview>> {
        return flowOf(
            safeApiCall {
                val response =
                    httpClient.get(urlString = "api/dashboard/fairs-overview/{fair_id}/bill-wise-overview") {
                        url {
                            path("api", "dashboard", "fairs-overview", fairId.toString(),"bill-wise-overview")
                            parameters.append("Date", "")
                        }
                    }.body<BillWiseOverview>()
                response
            }
        )
    }
}