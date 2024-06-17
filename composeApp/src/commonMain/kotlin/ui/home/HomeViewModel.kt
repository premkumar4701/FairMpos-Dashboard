package ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.fairdashboard.FairDashboardService
import api.fairdashboard.FairType
import api.fairdashboard.FairView
import datastore.DataStoreDaoImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import utils.Result
import utils.getMessage

class HomeViewModel(
    private val fairDashboardService: FairDashboardService,
    private val dataStoreDaoImpl: DataStoreDaoImpl
) : ViewModel() {
  var isShowLoading by mutableStateOf(false)
    private set

  private val _fetchFairData = MutableStateFlow(FairView())
  val fetchFairData: Flow<FairView> = _fetchFairData

  private val _isUnAuthorized = MutableStateFlow(false)
  val isUnAuthorized: Flow<Boolean> = _isUnAuthorized

  private val _isNetWorkError = MutableStateFlow(false)
  val isNetWorkError: Flow<Boolean> = _isNetWorkError

  private val _serviceError = MutableStateFlow("")
  val serviceError: Flow<String> = _serviceError

  fun loadFairDashboard(value: FairType) {
    isShowLoading = true
    _fetchFairData.value = FairView(emptyList())
    viewModelScope.launch {
      fairDashboardService.getFairsBy(value.code).collect { result ->
        when (result) {
          is Result.Success -> {
            val fairView =
                FairView(
                    fairList = result.value.data,
                    fairType = FairType.entries.find { it.code == value.code }!!)
            _fetchFairData.value = fairView
          }
          is Result.Failure -> {
            _serviceError.value = result.message?.getMessage() ?: "Something went wrong"
          }
          is Result.Unauthorized -> {
            dataStoreDaoImpl.setAuthenticated(false)
            _isUnAuthorized.value = true
          }
        }
        isShowLoading = false
      }
    }
  }
}
