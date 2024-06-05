package api.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(

    @SerialName("Username")
    val userName: String,

    @SerialName("password")
    val password: String
)