package api.billingdeviceinfo

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class BillingDeviceInfoService constructor(private val httpClient: HttpClient) :
    ServiceRepository() {


    suspend fun getBillingDevicesInfo(
        fairId: Long,
    ): Flow<ResultState<FairDashboardResponse>> {
        return flowOf(
            safeApiCall {
                val response =
                    httpClient.get(urlString = "/api/dashboard/fairs/{fairid}/billing-device-info") {
                        url {
                            path("api", "dashboard", "fairs", fairId.toString(), "bestsellers")
                        }
                    }.body<FairDashboardResponse>()
                response
            }
        )
    }
}