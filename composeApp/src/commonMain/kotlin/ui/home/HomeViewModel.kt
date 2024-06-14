package ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.fairdashboard.FairDashboardService
import api.fairdashboard.FairType
import api.fairdashboard.FairView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import utils.Result

class HomeViewModel(private val fairDashboardService: FairDashboardService) : ViewModel() {
  var isShowLoading by mutableStateOf(false)
    private set

  private val _fetchFairData = MutableStateFlow(FairView())
  val fetchFairData: Flow<FairView> = _fetchFairData

  private val _isUnAuthorized = MutableStateFlow(false)
  val isUnAuthorized: Flow<Boolean> = _isUnAuthorized

  private val _isNetWorkError = MutableStateFlow(false)
  val isNetWorkError: Flow<Boolean> = _isNetWorkError

  private val _isServiceError = MutableStateFlow(false)
  val isServiceError: Flow<Boolean> = _isServiceError

  fun loadFairDashboard(value: FairType) {
    _fetchFairData.value = FairView(emptyList())
    viewModelScope.launch {
      fairDashboardService.getFairsBy(value.code).collect { result ->
        when (result) {
          //                    is Result.Success<> -> {
          //                        val fairView = FairView(
          //                            fairList = result.value,
          //                            fairType = FairType.values().find { it.code == status.value
          // }!!
          //                        )
          //                        _fetchFairData.value = fairView
          //                    }
          //                    is Result.Error -> {
          //                        if (result.statusCode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
          //                            LSPref.isAuthenticated = false
          //                            _isUnAuthorized.value = Event(Unit)
          //                        }else{
          //                            _serviceError.value = Event(result.messages.getMessage())
          //                        }
          //                    }
          //                    is Result.NoConnection -> {
          //                        _networkError.value = Event(Unit)
          //                    }
          is Result.Success -> {
            val fairView =
                FairView(
                    fairList = result.value.data,
                    fairType = FairType.entries.find { it.code == value.code }!!)
            _fetchFairData.value = fairView
          }
          is Result.Failure -> {
            val ksj = result.message
          }
          is Result.Unauthorized -> {
            val kk = result.statusCode
            isShowLoading = true
          }
        }
      }
    }
  }
}
