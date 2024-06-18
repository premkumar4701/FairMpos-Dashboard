package ui.billingdeviceinfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.billingdeviceinfo.BillingDeviceInfoService
import api.billingdeviceinfo.BillingDevicesInfo
import datastore.DataStoreDaoImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import utils.Result
import utils.getMessage

class BillingDeviceInfoViewModel(
    private val billingDeviceInfoService: BillingDeviceInfoService,
    private val dataStoreDaoImpl: DataStoreDaoImpl
) : ViewModel() {

  private val _billingDeviceInfo = MutableStateFlow<BillingDevicesInfo?>(null)
  val billingDevicesInfo: Flow<BillingDevicesInfo?> = _billingDeviceInfo

  var isShowLoading by mutableStateOf(false)
    private set

  private val _serviceError = MutableStateFlow("")
  val serviceError: Flow<String> = _serviceError

  private val _isUnAuthorized = MutableStateFlow(false)
  val isUnAuthorized: Flow<Boolean> = _isUnAuthorized

  suspend fun getBillingDevicesInfo(fairId: Long) {
    isShowLoading = true
      billingDeviceInfoService.getBillingDevicesInfo(fairId).collect { result ->
        when (result) {
          is Result.Success -> {
            _billingDeviceInfo.value = result.value.data
          }
          is Result.Failure -> {
            _serviceError.value = result.message?.getMessage() ?: "Something went wrong"
          }
          is Result.Unauthorized -> {
            dataStoreDaoImpl.setAuthenticated(isAuthenticated = false)
            _isUnAuthorized.value = true
          }
        }
        isShowLoading = false
      }
  }
}
