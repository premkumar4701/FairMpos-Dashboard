package ui.login

data class LoginView(var userName: String = "", var password: String = "")

data class LoginErrorView(
    var userNameError: String? = null,
    var passwordError: String? = null
)
