package api.billitemsoverview

import api.billingdeviceinfo.FairDashboardResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class BillItemsOverviewService constructor(private val httpClient: HttpClient) :
    ServiceRepository() {

    suspend fun getBillItemsOverview(
        billId: Long,
    ): Flow<ResultState<BillItemsOverviewResponse>> {
        return flowOf(
            safeApiCall {
                val response =
                    httpClient.get(urlString = "api/dashboard/bill-overview/{bill_id}") {
                        url {
                            path("api", "dashboard", "bill-overview", billId.toString())
                        }
                    }.body<BillItemsOverviewResponse>()
                response
            }
        )
    }
}