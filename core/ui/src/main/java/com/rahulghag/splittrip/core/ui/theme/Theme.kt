package com.rahulghag.splittrip.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary             = Brand400,
    onPrimary           = Neutral0,
    primaryContainer    = Brand50,
    onPrimaryContainer  = Brand600,
    secondary           = Brand300,
    onSecondary         = Neutral0,
    secondaryContainer  = Brand100,
    onSecondaryContainer = Brand600,
    background          = Color(0xFFF2F1ED),
    onBackground        = Neutral900,
    surface             = Neutral0,
    onSurface           = Neutral800,
    surfaceVariant      = Neutral50,
    onSurfaceVariant    = Neutral600,
    outline             = Neutral200,
    outlineVariant      = Neutral100,
    error               = ErrorRed,
    onError             = Neutral0,
    errorContainer      = ErrorContainer,
    onErrorContainer    = ErrorRed,
)

private val DarkColorScheme = darkColorScheme(
    primary             = Brand300,
    onPrimary           = Brand800,
    primaryContainer    = Brand600,
    onPrimaryContainer  = Brand100,
    secondary           = Brand200,
    onSecondary         = Brand700,
    secondaryContainer  = Brand700,
    onSecondaryContainer = Brand100,
    background          = Color(0xFF111009),
    onBackground        = Neutral100,
    surface             = Color(0xFF1C1B18),
    onSurface           = Neutral200,
    surfaceVariant      = Color(0xFF2A2924),
    onSurfaceVariant    = Neutral400,
    outline             = Neutral700,
    outlineVariant      = Neutral800,
    error               = Color(0xFFFFB4AB),
    onError             = Color(0xFF690005),
    errorContainer      = Color(0xFF93000A),
    onErrorContainer    = Color(0xFFFFDAD6),
)

// Extra semantic colors not covered by Material color slots
data class SplitTripExtendedColors(
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val info: Color,
    val infoContainer: Color,
    val memberColors: List<Color>,
    val memberContainerColors: List<Color>,
)

val LocalExtendedColors = staticCompositionLocalOf {
    SplitTripExtendedColors(
        success              = Success,
        successContainer     = SuccessContainer,
        warning              = Warning,
        warningContainer     = WarningContainer,
        info                 = InfoBlue,
        infoContainer        = InfoContainer,
        memberColors         = MemberColors,
        memberContainerColors = MemberContainerColors,
    )
}

@Composable
fun SplitTripTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep false — we want brand colors not wallpaper colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalExtendedColors provides LocalExtendedColors.current,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = SplitTripTypography,
            shapes      = SplitTripShapes,
            content     = content,
        )
    }
}

// Convenience accessor for extended colors
// Usage: MaterialTheme.extendedColors.success
val MaterialTheme.extendedColors: SplitTripExtendedColors
    @Composable get() = LocalExtendedColors.current
