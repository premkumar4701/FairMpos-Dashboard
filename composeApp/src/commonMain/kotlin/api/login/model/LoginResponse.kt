package api.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(

    @SerialName("Success")
    val success: Boolean,

    @SerialName("Messages")
    val message: List<String>,

    @SerialName("Data")
    val data: Boolean
)