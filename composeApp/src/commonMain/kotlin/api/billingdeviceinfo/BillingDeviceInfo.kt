package api.billingdeviceinfo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FairDashboardResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: BillingDevicesInfo
)

@Serializable
data class BillingDevicesInfo(

    @SerialName("NoOfDevices")
    val noOfDevices: Int,

    @SerialName("BillingDays")
    val billingDays: Int,

    @SerialName("FairDevicesLastSync")
    val fairDevicesLastSync: List<FairDevicesLastSync>,
)

@Serializable
data class FairDevicesLastSync(

    @SerialName("Name")
    var name: String,

    @SerialName("Email")
    var email: String? = null,

    @SerialName("MobileNo")
    var mobileNo: String? = null,

    //TODO LastSyncDate instant need to be handled

)