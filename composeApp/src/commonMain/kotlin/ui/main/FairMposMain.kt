package ui.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import enum.FairMposScreens
import enum.PlatFormType
import getPlatform
import theme.colorText
import ui.home.HomeScreen
import ui.login.LoginScreen
import ui.placeholder.PlaceHolderScreen
import ui.setup.SetupScreen

@Composable
fun FairMposAppbar(
    currentScreen: FairMposScreens,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canNavigateBack = when(currentScreen) {
        FairMposScreens.Login -> false
        FairMposScreens.Setup -> false
        FairMposScreens.PlaceHolder -> false
        FairMposScreens.Home -> false
        FairMposScreens.NoConnection -> false
        else -> true
    }
  TopAppBar(
      title = { Text(currentScreen.name) },
      modifier = modifier,
      navigationIcon =
          if (canNavigateBack) {
            {
              IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Button",
                )
              }
            }
          } else null,
      backgroundColor = colorText,
      contentColor = Color.White)
}

@Composable
fun FairMposApp(navController: NavHostController = rememberNavController()) {
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentScreen =
      FairMposScreens.valueOf(
          backStackEntry?.destination?.route ?: FairMposScreens.PlaceHolder.name)
  val snackbarHostState = remember { SnackbarHostState() }
  Scaffold(
      topBar = {
        FairMposAppbar(
            currentScreen = currentScreen,
            navigateUp = { navController.navigateUp() })
      },
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FairMposScreens.PlaceHolder.name,
            modifier =
                Modifier.fillMaxSize().padding(innerPadding),
            enterTransition = {
              slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(900))
            },
            exitTransition = {
              slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(900))
            },
            popEnterTransition = {
              slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(900))
            },
            popExitTransition = {
              slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(900))
            }) {
              composable(route = FairMposScreens.PlaceHolder.name) {
                PlaceHolderScreen(navController, modifier = Modifier.fillMaxSize())
              }
              composable(route = FairMposScreens.Setup.name) {
                SetupScreen(navController, modifier = responsivePadding())
              }
              composable(route = FairMposScreens.Login.name) {
                LoginScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController,
                    snackbarHostState = snackbarHostState)
              }
              composable(route = FairMposScreens.Home.name) {
                HomeScreen(modifier = Modifier.fillMaxSize(), navController)
              }
            }
      }
}

fun isDesktop(): Boolean {
  val platform = getPlatform()
  return when (platform.name) {
    PlatFormType.Desktop.name -> true
    else -> false
  }
}

@Composable
fun responsivePadding(): Modifier {
  val horizontalPadding = if (isDesktop()) 150.dp else 16.dp
  val verticalPadding = if (isDesktop()) 50.dp else 16.dp
  return Modifier.fillMaxSize().padding(horizontal = horizontalPadding, vertical = verticalPadding)
}
