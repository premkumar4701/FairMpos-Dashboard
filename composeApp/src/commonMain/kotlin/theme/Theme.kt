package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration

private val LightColors = lightColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryVariant,
    secondary = colorSecondary,
    secondaryVariant = colorSecondaryVariant,
    background =  colorScreenBackground
)

private val DarkColors = darkColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryVariant,
    secondary = colorSecondary,
    secondaryVariant = colorSecondaryVariant,
    background =  colorScreenBackground
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
){
    val colorScheme = when {
        darkTheme -> DarkColors
        else -> LightColors
    }
   MaterialTheme(
       colors = colorScheme,
       typography = MaterialTheme.typography,
       content = content
   )
}