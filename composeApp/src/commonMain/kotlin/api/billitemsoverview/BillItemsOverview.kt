package api.billitemsoverview

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillItemsOverviewResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: BillItemsOverview
)

@Serializable
data class BillItemsOverview(

    @SerialName("ID")
    val id: Long,

    @SerialName("Code")
    val code: Long,

    @SerialName("Name")
    val name: String,

    @SerialName("School")
    val school: String,

    //TODO StartDate Instant need to handle

    //TODO EndDate Instant need to handle

    @SerialName("BillNo")
    val billNo: String,

    //TODO BillDateTime Instant need to handle

    @SerialName("CustomerName")
    val customerName: String,

    @SerialName("Remarks")
    val remarks: String?,

    @SerialName("Paymode")
    val paymode: String,

    @SerialName("NettValue")
    val nettValue: Double,

    @SerialName("TotalQty")
    val totalQty: Int,

    @SerialName("BreakUp")
    val billItems: List<BillItems>
)

@Serializable
data class BillItems(

    @SerialName("Name")
    val name: String,

    @SerialName("Code")
    val code: String,

    @SerialName("Qty")
    val qty: Int,

    @SerialName("Discount")
    val discount: Double?,

    @SerialName("Value")
    val value: Double,

    @SerialName("Currency")
    val currency: String

)
