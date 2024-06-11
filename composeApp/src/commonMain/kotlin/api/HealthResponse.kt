package api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    @SerialName("Success") val success: Boolean,
    @SerialName("Messages") val messages: List<String>,
    @SerialName("Data") val date: Boolean
)
