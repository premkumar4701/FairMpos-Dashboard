package api.login

import api.login.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Result

class LoginService constructor(private val httpClient: HttpClient) {

  suspend fun login(userLogin: LoginDto): Flow<Result> {
    return flow {
      val response: HttpResponse =
          httpClient.post("https://mpos-stage.lspl.dev/api/dashboard/login") {
            contentType(ContentType.Application.Json)
            setBody(userLogin)
          }
      when (val statusCode = response.status) {
        HttpStatusCode.OK -> {
          val loginResponse: LoginResponse = response.body()
          if (loginResponse.success) {
            emit(Result.Success(loginResponse, statusCode))
          } else {
            emit(Result.Failure(statusCode, loginResponse.message))
          }
        }
        HttpStatusCode.Unauthorized -> {
          emit(Result.Unauthorized(statusCode, "Unauthorized"))
        }
      }
    }
  }
}
