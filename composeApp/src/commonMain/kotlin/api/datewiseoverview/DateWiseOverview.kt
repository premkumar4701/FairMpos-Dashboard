package api.datewiseoverview

import api.billwiseoverview.BillWiseOverview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DateWiseOverviewResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: DateWiseOverview
)

@Serializable
data class DateWiseOverview(

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

    //TODO LastBillDate need to be include

    @SerialName("BreakUp")
    val dateWiseOverviewView: List<DateWiseOverviewView>
)

@Serializable
data class DateWiseOverviewView(

    @SerialName("CollectionDate")
    val collectionDate: String,

    @SerialName("TotalNettSales")
    val totalNettSales: Int,

    @SerialName("TotalNettSalesValue")
    val totalNettSalesValue: Double,

    @SerialName("TotalCancelledBills")
    val totalCancelledBills: Int,

    @SerialName("TotalCancelledBillsValue")
    val totalCancelledBillsValue: Double,

    @SerialName("TotalReturnedBills")
    val totalReturnedBills: Int,

    @SerialName("TotalReturnedBillsValue")
    val totalReturnedBillsValue: Double,
    )
