package ui.placeholder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ui.FairMposScreens

class PlaceHolderViewModel : ViewModel() {

  private val isSetup = MutableStateFlow(true)
  private val isAuthorized = MutableStateFlow(true)

  fun determineInitialScreen(): FairMposScreens {
    return when {
      isSetup.value -> FairMposScreens.Setup
      isAuthorized.value -> FairMposScreens.Login
      else -> FairMposScreens.Home
    }
  }
}
