package api.bestsellers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BestSellersResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: List<BestSellers>
)

@Serializable
data class BestSellers(

    @SerialName("SerialNo")
    val serialNo: Int,

    @SerialName("ItemCode")
    val itemCode: String,

    @SerialName("ItemName")
    val itemName: String,

    @SerialName("NettSaleValue")
    val nettSaleValue: Double,

    @SerialName("NettSaleQty")
    val nettSaleQty: Int,

    @SerialName("Currency")
    val currency: String
)

enum class SortBy(val value: Int, val text: String) {
    Value(1, "Value"),
    Qty(2, "Qty")
}