package ui.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import datastore.DataStoreDaoImpl
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.invalid_code
import fairmpos_dashboard.composeapp.generated.resources.invalid_url_address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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

  private suspend fun isUrlValid(endPoint: String): Boolean {
    // TODO need a ktor implementation
    //    return withContext(Dispatchers.IO) {
    //      try {
    //        val client = OkHttpClient()
    //        val request = Request.Builder().url("${url}api/health").get().build()
    //        val response = client.newCall(request).execute()
    //        response.isSuccessful &&
    //                response.body?.let {
    //                  val type =
    //                    Types.newParameterizedType(
    //                      LsResponse::class.java,
    //                      Boolean::class.javaObjectType
    //                    )
    //                  val pullResponse =
    //                    RetrofitFactory.moshi.adapter<LsResponse<Boolean>>(type)
    //                      .fromJson(it.source())!!
    //                  pullResponse.data == true
    //                } ?: false
    //      } catch (e: Exception) {
    //        Timber.e("Error on isUrlValid $e")
    //        false
    //      }
    //    }
    return endPoint == "https://lspl.mposv2-stage.lspl.dev/"
  }

  private fun getEndpoint(setupModel: SetupModel): String {
    val cleanedSetupCode = cleanCode(setupModel.organizationCode!!)
    // TODO flavours need to be implemented
    //    if (BuildConfig.DEBUG && cleanedSetupCode.startsWith("!", ignoreCase = true)) {
    //      return ensureTrailingSlash(cleanedSetupCode.removePrefix("!"))
    //    }
    //    if (cleanedSetupCode == BuildConfig.TESTING_KEY_FOR_PLAY_CONSOLE) {
    //      return ensureTrailingSlash(BuildConfig.STAGING_URL)
    //    }
    //    val suffix =
    //        when {
    //          BuildConfig.DEBUG -> {
    //            BuildConfig.STAGING_SUFFIX
    //          }
    //          else -> {
    //            BuildConfig.PRODUCTION_SUFFIX
    //          }
    //        }
    return ensureTrailingSlash("https://${cleanedSetupCode}.mposv2-stage.lspl.dev")
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

  fun setOrganizationCode(value: String) {
    setupModel =
        SetupModel(
            organizationCode = value, organizationCodeError = setupModel.organizationCodeError)
  }
}
