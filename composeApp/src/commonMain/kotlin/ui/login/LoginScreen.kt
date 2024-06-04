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
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.baseline_account_circle_24
import fairmpos_dashboard.composeapp.generated.resources.baseline_lock_24
import fairmpos_dashboard.composeapp.generated.resources.eye
import fairmpos_dashboard.composeapp.generated.resources.eye_off
import fairmpos_dashboard.composeapp.generated.resources.login_fragment_description
import fairmpos_dashboard.composeapp.generated.resources.login_icon
import fairmpos_dashboard.composeapp.generated.resources.login_label
import fairmpos_dashboard.composeapp.generated.resources.password
import fairmpos_dashboard.composeapp.generated.resources.proceed
import fairmpos_dashboard.composeapp.generated.resources.username
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import theme.colorLabel
import theme.colorProgressBar
import theme.colorText
import theme.colorTextInputLayoutStroke

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavHostController) {
  var isShowLoading by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
      modifier = modifier.fillMaxSize().padding(16.dp),
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

        if (isShowLoading) {
          CircularProgressIndicator(color = colorProgressBar)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(Res.string.login_label),
            color = colorLabel,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it},
            label = { Text(text = stringResource(Res.string.username)) },
            leadingIcon = {
              Icon(
                  painter = painterResource(Res.drawable.baseline_account_circle_24),
                  contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorTextInputLayoutStroke, focusedLabelColor = colorText))

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(text = stringResource(Res.string.password)) },
            leadingIcon = {
              Icon(
                  painter = painterResource(Res.drawable.baseline_lock_24),
                  contentDescription = null)
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors =
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorTextInputLayoutStroke, focusedLabelColor = colorText),
            trailingIcon = {
                val image = if (passwordVisible)
                    Res.drawable.eye
                else Res.drawable.eye_off

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(painter = painterResource(image), contentDescription = description)
                }
            })

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { isShowLoading = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
              Text(text = stringResource(Res.string.proceed), color = Color.White)
            }
      }
}
