package api.fairoverview

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FairOverviewResponse(
    @SerialName("Success") val success: Boolean,
    @SerialName("Messages") val message: List<String>,
    @SerialName("Data") val data: FairOverView
)

@Serializable
data class FairOverView(
    @SerialName("ID") val id: Long,
    @SerialName("Code") val code: Long,
    @SerialName("Name") val name: String,
    @SerialName("School") val school: String?,
    @SerialName("StartDate") val startDate: Instant,
    @SerialName("EndDate") val endDate: Instant,
    @SerialName("TotalDevices") val noOfDevices: Int,
    @SerialName("TotalBillingDays") val noOfBillingDays: Int,
    @SerialName("TotalBills") val noOfBills: Int,
    @SerialName("SaleValue") val salesValue: Double,
    @SerialName("TotalNettValue") val totalNettValue: Double,
    @SerialName("ReturnedValue") val returnedValue: Double,
    @SerialName("CancelledValue") val cancelledValue: Double,
    @SerialName("LastBillDate") val lastBillDate: Instant,
    @SerialName("TotalCancelledBills") val totalCancelledBills: Int,
    @SerialName("TotalNettSoldQty") val totalNettSoldQty: Int
)
