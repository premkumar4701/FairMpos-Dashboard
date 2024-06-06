package api.login

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import utils.ResultState
import utils.ServiceRepository

class LoginService constructor( private val httpClient: HttpClient):ServiceRepository() {

    suspend fun login(userLogin: LoginDto):Flow<ResultState<Boolean>>{
        return flowOf(
            safeApiCall {
                val response = httpClient.post(urlString = "api/dashboard/login") {
                    setBody(userLogin)
                }.body<Boolean>()
                response
            }
        )
    }
}