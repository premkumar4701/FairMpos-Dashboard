package ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import enum.FairMposScreens
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.baseline_account_circle_24
import fairmpos_dashboard.composeapp.generated.resources.baseline_lock_24
import fairmpos_dashboard.composeapp.generated.resources.eye
import fairmpos_dashboard.composeapp.generated.resources.eye_off
import fairmpos_dashboard.composeapp.generated.resources.login_fragment_description
import fairmpos_dashboard.composeapp.generated.resources.login_icon
import fairmpos_dashboard.composeapp.generated.resources.login_label
import fairmpos_dashboard.composeapp.generated.resources.ok
import fairmpos_dashboard.composeapp.generated.resources.password
import fairmpos_dashboard.composeapp.generated.resources.password_error
import fairmpos_dashboard.composeapp.generated.resources.proceed
import fairmpos_dashboard.composeapp.generated.resources.username
import fairmpos_dashboard.composeapp.generated.resources.username_error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import theme.colorLabel
import theme.colorProgressBar
import theme.colorText
import theme.colorTextInputLayoutStroke

@Composable
fun LoginScreen(
    modifier: Modifier,
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinInject(),
    snackbarHostState: SnackbarHostState
) {
  var passwordVisible by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()
  val usernameFocusRequester = remember { FocusRequester() }
  val passwordFocusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(true) {
    launch {
      usernameFocusRequester.requestFocus()
      viewModel.success.collect { success ->
        if (success) {
          navHostController.navigate(FairMposScreens.Home.name) {
            popUpTo(FairMposScreens.Login.name) { inclusive = true }
          }
        }
      }
    }
    launch {
      viewModel.error.collect { errorMessage ->
        if (errorMessage != "") {
          snackbarHostState.showSnackbar(
              message = errorMessage, actionLabel = getString(Res.string.ok))
          viewModel.clearError()
        }
      }
    }
    launch {
      viewModel.loginErrorViewFlow.collect { loginErrorView ->
        if (!loginErrorView.userNameError.isNullOrEmpty()) {
          usernameFocusRequester.requestFocus()
          keyboardController?.show()
          viewModel.clearError()
        } else if (!loginErrorView.passwordError.isNullOrEmpty()) {
          passwordFocusRequester.requestFocus()
          keyboardController?.show()
          viewModel.clearError()
        }
      }
    }
  }

  Column(
      modifier =
          modifier.fillMaxSize().padding(16.dp).verticalScroll(state = rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(Res.drawable.login_icon),
            contentDescription = null,
            modifier = Modifier.size(120.dp))

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(Res.string.login_fragment_description),
            textAlign = TextAlign.Center,
            color = colorLabel,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp).padding(10.dp))
        Spacer(modifier = Modifier.height(100.dp))

        if (viewModel.isShowLoading) {
          CircularProgressIndicator(color = colorProgressBar)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(Res.string.login_label),
            color = colorLabel,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))

        OutlinedTextField(
            value = viewModel.loginView.userName,
            onValueChange = { userName -> viewModel.setUserName(userName) },
            label = { Text(text = stringResource(Res.string.username)) },
            leadingIcon = {
              Icon(
                  painter = painterResource(Res.drawable.baseline_account_circle_24),
                  contentDescription = null)
            },
            modifier =
                Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester = usernameFocusRequester),
            colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorTextInputLayoutStroke, focusedLabelColor = colorText),
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            isError = !viewModel.loginErrorView.userNameError.isNullOrEmpty())
        if (!viewModel.loginErrorView.userNameError.isNullOrEmpty()) {
          Text(
              text = stringResource(Res.string.username_error),
              color = MaterialTheme.colors.error,
              modifier = Modifier.padding(start = 16.dp).align(alignment = Alignment.Start),
              style = MaterialTheme.typography.caption,
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.loginView.password,
            onValueChange = { password -> viewModel.setPassword(password) },
            label = { Text(text = stringResource(Res.string.password)) },
            leadingIcon = {
              Icon(
                  painter = painterResource(Res.drawable.baseline_lock_24),
                  contentDescription = null)
            },
            visualTransformation =
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier =
                Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester = passwordFocusRequester),
            colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorTextInputLayoutStroke, focusedLabelColor = colorText),
            trailingIcon = {
              val image = if (passwordVisible) Res.drawable.eye else Res.drawable.eye_off

              val description = if (passwordVisible) "Hide password" else "Show password"

              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = painterResource(image), contentDescription = description)
              }
            },
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { scope.launch { viewModel.onLogin() } }),
            isError = !viewModel.loginErrorView.passwordError.isNullOrEmpty())
        if (!viewModel.loginErrorView.passwordError.isNullOrEmpty()) {
          Text(
              text = stringResource(Res.string.password_error),
              color = MaterialTheme.colors.error,
              modifier = Modifier.padding(start = 16.dp).align(alignment = Alignment.Start),
              style = MaterialTheme.typography.caption,
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
              scope.launch {
                usernameFocusRequester.freeFocus()
                viewModel.onLogin()
              }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
              Text(text = stringResource(Res.string.proceed), color = Color.White)
            }
      }
}
