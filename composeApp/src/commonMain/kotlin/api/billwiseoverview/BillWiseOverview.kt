package api.billwiseoverview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BillWiseOverview(

    @SerialName("ID")
    val id: Long,

    @SerialName("Code")
    val code: Long,

    @SerialName("Name")
    val name: String,

    @SerialName("School")
    val school: String,

    //TODO StartDate need to be include

    //TODO EndDate need to be include

    @SerialName("BreakUp")
    val billWiseOverviewView: List<BillWiseOverviewView>
)

@Serializable
data class BillWiseOverviewView(

    @SerialName("BillID")
    val billId: Long,

    @SerialName("BillNo")
    val billNo: String,

    //TODO BillDate need to be include

    @SerialName("TotalItems")
    val totalItems: Int,

    @SerialName("TotalQty")
    val totalQty: Int,

    @SerialName("NettSales")
    val nettSales: Double,

    @SerialName("ReturnedValue")
    val returnedValue: Double,

    @SerialName("CancelledValue")
    val cancelledValue: Double,

    //TODO CreatedAtOrigin need to be include

    //TODO CreatedAtService need to be include

    @SerialName("NettValue")
    val nettValue: Double,

    )