package work.aijiu.nfcactuator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
//    primary = Blue200,
//    primaryVariant = Purple700,
//    secondary = Teal200,
//    background = Color.Black,
//    surface = Color.Black,
//    onPrimary = Color.Black,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//
//    onError = Color.Red,
//    secondaryVariant = Teal200,

    primary = Blue200,
    primaryVariant = white300,
    secondary = Blue200,
    background = gray900,
    surface = gray800,
    onPrimary = gray900,
    onSecondary = gray900,
    onBackground = taupe100,
    onSurface = white800,
)

private val LightColorPalette = lightColors(
//    primary = Blue200,
//    primaryVariant = Purple700,
//    secondary = Teal200,
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,

    primary = Blue200,
    primaryVariant = gray600,
    secondary = rust600,
    background = taupe100,
    surface = white850,
    onPrimary = white,
    onSecondary = white,
    onBackground = taupe800,
    onSurface = gray800,

)

@Composable
fun NfcActuatorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}