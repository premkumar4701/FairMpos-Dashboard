package ui.placeholder

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import ui.FairMposScreens

@Composable
fun PlaceHolderScreen(navHostController: NavHostController, modifier: Modifier){
    val viewModel = PlaceHolderViewModel()
    val initialScreen = viewModel.determineInitialScreen()
    navHostController.navigate(initialScreen.name){
        popUpTo(FairMposScreens.PlaceHolder.name) { inclusive = true }
    }
}