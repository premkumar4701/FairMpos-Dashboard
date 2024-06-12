package ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import api.login.LoginDto
import api.login.LoginService
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.password_error
import fairmpos_dashboard.composeapp.generated.resources.username_error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import utils.Result
import utils.getMessage

class LoginViewModel constructor(private val loginService: LoginService) : KoinComponent {
  private val viewModelScope = CoroutineScope(Dispatchers.IO)

  var loginView by mutableStateOf(LoginView())
    private set

  var loginErrorView by mutableStateOf(LoginErrorView())
    private set

  var isShowLoading by mutableStateOf(false)
    private set

  private val _success = MutableStateFlow(false)
  val success: Flow<Boolean> = _success

  private val _loginErrorViewFlow = MutableStateFlow(LoginErrorView())
  val loginErrorViewFlow: Flow<LoginErrorView> = _loginErrorViewFlow

  private val _error = MutableStateFlow("")
  val error: Flow<String> = _error

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

    viewModelScope.launch {
      loginService
          .login(
              LoginDto(userName = loginView.userName.trim(), password = loginView.password.trim()))
          .collect { result ->
            when (result) {
              is Result.Success -> {
                // TODO: LSPref.isAuthenticated = true, Need to be set here.
                if (result.response.success) {
                  isShowLoading = false
                  _success.value = result.response.data
                } else {
                  isShowLoading = false
                  _error.value = result.response.message.getMessage()
                }
              }
              is Result.Unauthorized -> {
                isShowLoading = false
                _error.value = "Invalid username and password"
              }
              is Result.Failure -> {
                isShowLoading = false
                _error.value = result.message.getMessage()
              }
            }
          }
    }
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
