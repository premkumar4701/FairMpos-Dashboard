package api.fairdashboard

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FairDashboardResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: List<FairDashboard>
)

@Serializable
data class FairDashboard(
    @SerialName("ID")
    val fairID: Long,

    @SerialName("Code")
    val code: Long,

    @SerialName("Name")
    val fairName: String,

    @SerialName("School")
    val schoolName: String ?,

    @SerialName("StartDate")
    val startDate: Instant,

    @SerialName("EndDate")
    val endDate: Instant,

    @SerialName("TotalBills")
    val totalBills: Int,

    @SerialName("TodayNettValue")
    val todayNettValue: Double,

    @SerialName("TodayNettSoldQty")
    val todayNettSoldQty: Int,

    @SerialName("TotalNettValue")
    val totalNettValue: Double,

    @SerialName("TotalNettSoldQty")
    val totalNettSoldQty: Int?,

    @SerialName("Status")
    val status: String,
)

enum class FairType(val code: Int, val text: String) {
    TODAY(3, "Today's Fairs"),
    ACTIVE(1, "Active Fairs"),
    CLOSED(2, "Closed Fairs"),
    ALL(0, "All Fairs")
}

data class FairView(
    var fairList: List<FairDashboard> = emptyList(),
    var fairType: FairType? = FairType.TODAY
)