package ui.placeholder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import datastore.DataStoreDaoImpl
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import enum.FairMposScreens

@Composable
fun PlaceHolderScreen(
    navHostController: NavHostController,
    modifier: Modifier,
    dataStoreDaoImpl: DataStoreDaoImpl = koinInject()
) {
  val scope = rememberCoroutineScope()
  scope.launch {
    if (!dataStoreDaoImpl.getSetup()) {
      navHostController.navigate(FairMposScreens.Setup.name) {
        popUpTo(FairMposScreens.PlaceHolder.name) { inclusive = true }
      }
    } else if (!dataStoreDaoImpl.getAuthenticated()) {
      navHostController.navigate(FairMposScreens.Login.name) {
        popUpTo(FairMposScreens.PlaceHolder.name) { inclusive = true }
      }
    } else {
      navHostController.navigate(FairMposScreens.Home.name) {
        popUpTo(FairMposScreens.PlaceHolder.name) { inclusive = true }
      }
    }
  }
}
