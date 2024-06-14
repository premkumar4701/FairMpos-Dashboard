package ui.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.code_hint
import fairmpos_dashboard.composeapp.generated.resources.ic_undraw_dashboard
import fairmpos_dashboard.composeapp.generated.resources.invalid_code
import fairmpos_dashboard.composeapp.generated.resources.organization_code_label
import fairmpos_dashboard.composeapp.generated.resources.submit
import fairmpos_dashboard.composeapp.generated.resources.welcome_message
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import theme.colorLabel
import theme.colorProgressBar
import theme.colorText
import theme.colorTextInputLayoutStroke
import enum.FairMposScreens

@Composable
fun SetupScreen(
    navHostController: NavHostController,
    modifier: Modifier,
    viewModel: SetupViewModel = koinInject()
) {
  val scope = rememberCoroutineScope()
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
    viewModel.success.collect { success ->
      if (success) {
        navHostController.navigate(FairMposScreens.Login.name) {
          popUpTo(FairMposScreens.Setup.name) { inclusive = true }
        }
      }
    }
  }
  Column(
      modifier = modifier.padding(top = 100.dp).verticalScroll(state = rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(Res.drawable.ic_undraw_dashboard),
            contentDescription = null,
            modifier = Modifier.width(300.dp).height(100.dp))

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = stringResource(Res.string.welcome_message),
            fontSize = 14.sp,
            color = colorLabel,
            modifier = Modifier.padding(horizontal = 16.dp).padding(10.dp),
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(100.dp))

        if (viewModel.isShowLoading) {
          CircularProgressIndicator(color = colorProgressBar)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = stringResource(Res.string.organization_code_label),
            color = colorText,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp))

        OutlinedTextField(
            value = viewModel.setupModel.organizationCode ?: "",
            onValueChange = { organizationCode -> viewModel.setOrganizationCode(organizationCode) },
            label = { Text(stringResource(Res.string.code_hint)) },
            colors =
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorTextInputLayoutStroke, focusedLabelColor = colorText),
            modifier =
                Modifier.fillMaxWidth().padding(horizontal = 16.dp).focusRequester(focusRequester),
            isError = !viewModel.setupModel.organizationCodeError.isNullOrEmpty(),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            singleLine = true)
        if (!viewModel.setupModel.organizationCodeError.isNullOrEmpty()) {
          keyboardController?.show()
          Text(
              text = stringResource(Res.string.invalid_code),
              color = MaterialTheme.colors.error,
              modifier = Modifier.padding(start = 16.dp).align(alignment = Alignment.Start),
              style = MaterialTheme.typography.caption,
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { scope.launch { viewModel.onSubmit() } },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
              Text(text = stringResource(Res.string.submit), color = Color.White)
            }

        val annotatedString = buildAnnotatedString {
          append("Developed by ")
          pushStringAnnotation(tag = "URL", annotation = "http://www.logicsoft.co.in")
          withStyle(
              style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("Logic Soft")
              }
          pop()
          append(", Chennai")
        }
        val uriHandler = LocalUriHandler.current
        ClickableText(
            text = annotatedString,
            modifier = Modifier.padding(16.dp),
            onClick = { offset ->
              annotatedString
                  .getStringAnnotations(tag = "URL", start = offset, end = offset)
                  .firstOrNull()
                  ?.let { annotation -> uriHandler.openUri(annotation.item) }
            })
      }
}
