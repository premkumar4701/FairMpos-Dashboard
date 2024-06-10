package ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.invalid_credentials
import fairmpos_dashboard.composeapp.generated.resources.password_error
import fairmpos_dashboard.composeapp.generated.resources.username_error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.getString

class LoginViewModel : ViewModel() {

  var loginView by mutableStateOf(LoginView())
    private set

  var loginErrorView by mutableStateOf(LoginErrorView())
    private set

  var isShowLoading by mutableStateOf(false)
    private set

  private val _success = MutableStateFlow(false)
  val success: Flow<Boolean> = _success

  private val _loginErrorViewFlow  = MutableStateFlow(LoginErrorView())
  val loginErrorViewFlow: Flow<LoginErrorView> = _loginErrorViewFlow

  private val _error = MutableStateFlow("")
  val error: Flow<String> = _error

  // TODO need to implemented after ktor
  // private val loginService: LoginService = LoginService.getInstance()

  fun setUserName(value: String) {
    loginView = loginView.copy(userName = value)
  }

  fun setPassword(value: String) {
    loginView = loginView.copy(password = value)
  }

  suspend fun onLogin() {
    isShowLoading = true
    if (loginValidation()) {
      loginErrorView = LoginErrorView()
      dashboardLogin()
    } else {
      isShowLoading = false
    }
  }

  private suspend fun dashboardLogin() {
    // TODO need to implemented after ktor
    //        viewModelScope.launch {
    //            when (val result =
    //                loginService.login(UserLogin(login.userName.trim(), login.password.trim()))) {
    //                is Result.Success -> {
    //                    if (result.value) {
    //                        LSPref.isAuthenticated = true
    //                        _success.value = Event(Unit)
    //                    }
    //                }
    //
    //                is Result.Error -> {
    //                    _loginError.value =
    // Event(application.getString(R.string.invalid_credentials))
    //                }
    //
    //                is Result.NoConnection -> Unit
    //            }
    //            _isLoading.value = false
    //        }
    if (loginView.userName.trim() == "prem" && loginView.password.trim() == "123") {
      _success.value = true
    } else {
      _error.value = getString(Res.string.invalid_credentials)
    }
    isShowLoading = false
  }

  private suspend fun loginValidation(): Boolean {
    if (loginView.userName.isEmpty()) {
      loginErrorView = LoginErrorView(userNameError = getString(Res.string.username_error))
      _loginErrorViewFlow.value = loginErrorView
      return false
    } else if (loginView.password.isEmpty()) {
      loginErrorView = LoginErrorView(passwordError = getString(Res.string.password_error))
      _loginErrorViewFlow.value = loginErrorView
      return false
    }
    return true
  }

  fun clearError() {
    _error.value = ""
    _loginErrorViewFlow.value = LoginErrorView()
  }
}
