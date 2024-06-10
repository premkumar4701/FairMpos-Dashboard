package api.datewiseoverview

import api.billwiseoverview.BillWiseOverview
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class DateWiseOverviewService(private val httpClient: HttpClient): ServiceRepository()  {
    suspend fun getDateWiseOverview(
        fairId: Long
    ): Flow<ResultState<DateWiseOverviewView>> {
        return flowOf(
            safeApiCall {
                val response =
                    httpClient.get(urlString = "api/dashboard/fairs-overview/{fair_id}/date-wise-overview") {
                        url {
                            path("api", "dashboard", "fairs-overview", fairId.toString(),"date-wise-overview")
                        }
                    }.body<DateWiseOverviewView>()
                response
            }
        )
    }
}