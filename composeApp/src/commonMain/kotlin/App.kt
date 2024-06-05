import androidx.compose.runtime.*
import di.appModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import theme.AppTheme
import ui.main.FairMposApp

@Composable
@Preview
fun App() {
  KoinApplication(application = {
    modules(appModule(true))
  }) {
    AppTheme { FairMposApp() }
  }
}
