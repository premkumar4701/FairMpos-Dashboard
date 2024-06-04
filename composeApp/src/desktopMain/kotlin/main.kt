import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import fairmpos_dashboard.composeapp.generated.resources.Res
import fairmpos_dashboard.composeapp.generated.resources.app_name
import fairmpos_dashboard.composeapp.generated.resources.logic_soft_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
  val state =
      rememberWindowState(
          placement = WindowPlacement.Floating,
          position = WindowPosition(Alignment.Center),
          isMinimized = false,
          width = 800.dp,
          height = 600.dp)

  Window(
      title = stringResource(Res.string.app_name),
      resizable = true,
      icon = painterResource(Res.drawable.logic_soft_logo),
      state = state,
      onCloseRequest = ::exitApplication) {
        App()
      }
}
