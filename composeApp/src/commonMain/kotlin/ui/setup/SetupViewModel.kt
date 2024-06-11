package ui.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import datastore.DataStoreDaoImpl
import api.HealthResponse
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.invalid_code
import fairmpos_dashboard.composeapp.generated.resources.invalid_url_address
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import ui.utils.UseCaseResult

class SetupViewModel(
  private val dataStoreDaoImpl: DataStoreDaoImpl
) : ViewModel() {
  var setupModel by mutableStateOf(SetupModel())
    private set

  var isShowLoading by mutableStateOf(false)
    private set

  private val _success = MutableStateFlow(false)
  val success: Flow<Boolean> = _success

  fun onSubmit() {
    isShowLoading = true
    viewModelScope.launch {
      when (val result = setupValidation(setupModel)) {
        is UseCaseResult.Success -> {
          val endPoint = getEndpoint(result.value)
          when {
            endPoint.isNullOrBlank() -> {
              isShowLoading = false
              setupModel =
                  SetupModel(
                      organizationCode = setupModel.organizationCode,
                      organizationCodeError = getString(Res.string.invalid_code))
            }
            endPoint.isNotBlank() -> {
              if ((endPoint.toLowerCase(Locale.current).startsWith("http://") ||
                  endPoint.toLowerCase(Locale.current).startsWith("https://")) &&
                  (endPoint.length > 7 || endPoint.length > 8)) {

                if (isUrlValid(endPoint)) {
                  isShowLoading = false
                  dataStoreDaoImpl.setBaseUrl(endPoint)
                  dataStoreDaoImpl.setSetup(true)
                  // TODO implementation need of sentry
                  //  Sentry.getCurrentHub().options.serverName = endPoint
                  setupModel =
                      SetupModel(
                          organizationCode = setupModel.organizationCode,
                          organizationCodeError = null)
                  _success.value = true
                } else {
                  isShowLoading = false
                  setupModel =
                      SetupModel(
                          organizationCode = setupModel.organizationCode,
                          organizationCodeError = getString(Res.string.invalid_url_address))
                }
              } else {
                isShowLoading = false
                setupModel =
                    SetupModel(
                        organizationCode = setupModel.organizationCode,
                        organizationCodeError = getString(Res.string.invalid_url_address))
              }
            }
          }
        }
        is UseCaseResult.Error -> {
          isShowLoading = false
          setupModel = result.error
        }
      }
    }
  }

  private fun getEndpoint(setupModel: SetupModel): String {
    // TODO: Need to be handled build variant wise
    val cleanedSetupCode = cleanCode(setupModel.organizationCode!!)
    val suffix = ".mposv2-stage.lspl.dev"
    return ensureTrailingSlash("https://${cleanedSetupCode}$suffix")
  }

  private fun cleanCode(code: String): String {
    return code.toLowerCase(Locale.current).replace("\\s".toRegex(), "")
  }

  private fun ensureTrailingSlash(baseUrl: String): String {
    return if (!baseUrl.endsWith("/")) {
      "$baseUrl/"
    } else {
      baseUrl
    }
  }

  private suspend fun setupValidation(setupModel: SetupModel): UseCaseResult<SetupModel> {
    val model = SetupModel()
    if (setupModel.organizationCode.isNullOrEmpty()) {
      model.organizationCodeError = getString(Res.string.invalid_code)
      model.organizationCode = setupModel.organizationCode
      return UseCaseResult.Error(model)
    }
    model.organizationCode = setupModel.organizationCode
    model.organizationCodeError = null
    return UseCaseResult.Success(model)
  }

  private val client = HttpClient {
    install(ContentNegotiation) {
      json(
          Json {
            ignoreUnknownKeys = true
            isLenient = true
          })
    }

    install(Logging) { level = LogLevel.INFO }
  }

  private suspend fun isUrlValid(url: String): Boolean {
    return try {
      val response: HttpResponse =
          client.get {
            url("${url}api/health")
            contentType(ContentType.Application.Json)
          }
      if (response.status == HttpStatusCode.OK) {
        val healthResponse: HealthResponse = response.body()
        println("Response: $healthResponse")
        healthResponse.date
      } else {
        println("Error: Received status code ${response.status}")
        false
      }
    } catch (e: Exception) {
      println("Error on isUrlValid: $e")
      setOrganizationCode(e.toString())
      false
    }
  }

  fun setOrganizationCode(value: String) {
    setupModel =
        SetupModel(
            organizationCode = value, organizationCodeError = setupModel.organizationCodeError)
  }
}
