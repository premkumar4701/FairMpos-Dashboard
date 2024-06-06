package api.login

import api.login.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class LoginService constructor( private val httpClient: HttpClient):ServiceRepository() {

    suspend fun login(userLogin: LoginDto):Flow<ResultState<LoginResponse>>{
        return flowOf(
            safeApiCall {
                val response = httpClient.post(urlString = "api/dashboard/login") {
                    contentType(ContentType.Application.Json)
                    setBody(userLogin)
                }.body<LoginResponse>()
                response
            }
        )
    }
}