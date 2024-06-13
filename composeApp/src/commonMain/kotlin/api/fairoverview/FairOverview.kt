package api.fairoverview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FairOverviewResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: FairOverView
)
@Serializable
data class FairOverView(
    @SerialName("ID")
    val id: Long,

    @SerialName("Code")
    val code: Long,

    @SerialName("Name")
    val name: String,

    @SerialName("School")
    val school: String,

    //TODO StartDate need to be included

    //TODO EndDate need to be included

    @SerialName("TotalDevices")
    val noOfDevices: Int,

    @SerialName("TotalBillingDays")
    val noOfBillingDays: Int,

    @SerialName("TotalBills")
    val noOfBills: Int,

    @SerialName("SaleValue")
    val salesValue: Double,

    @SerialName("TotalNettValue")
    val totalNettValue: Double,

    @SerialName("ReturnedValue")
    val returnedValue: Double,

    @SerialName("CancelledValue")
    val cancelledValue: Double,

    //TODO  LastBillDate need to be included

    @SerialName("TotalCancelledBills")
    val totalCancelledBills: Int,

    @SerialName("TotalNettSoldQty")
    val totalNettSoldQty: Int
)