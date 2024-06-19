package ui.fairoverview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import api.fairoverview.FairOverView
import api.fairoverview.FairOverviewService
import datastore.DataStoreDaoImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utils.Result
import utils.getMessage

class FairOverviewViewModel(
    private val fairOverviewService: FairOverviewService,
    private val dataStoreDaoImpl: DataStoreDaoImpl,
    private val fairId: Long,
    private val hasBills: Boolean
) : ViewModel() {

  private val _fairOverview = MutableStateFlow<FairOverView?>(null)
  val fairOverview: StateFlow<FairOverView?> = _fairOverview

  private val _isUnAuthorized = MutableStateFlow(false)
  val isUnAuthorized: Flow<Boolean> = _isUnAuthorized
  var isLoading by mutableStateOf(false)
    private set

  private val _error = MutableStateFlow("")
  val error: Flow<String> = _error

  private val _networkError = MutableStateFlow<Unit?>(null)
  val networkError: StateFlow<Unit?> = _networkError

  init {
    if (hasBills) loadFairOverview()
  }

  fun loadFairOverview() {
    isLoading = true
    viewModelScope.launch {
      fairOverviewService.getFairOverviewById(fairId).collect { result ->
        when (result) {
          is Result.Success -> {
            _fairOverview.value = result.value.data
          }
          is Result.Failure -> {
            _error.value = result.message?.getMessage() ?: "Something went wrong"
          }
          is Result.Unauthorized -> {
            dataStoreDaoImpl.setAuthenticated(false)
            _isUnAuthorized.value = true
          }
        }
        isLoading = false
      }
    }
  }
}
