package com.example.whoareyou.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

private val AppColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = CardBackground,
    primaryContainer = PrimaryLight,
    background = Background,
    surface = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun WhoAreYouTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}

// Gradient brush (used in composables)
val HeroGradient = Brush.verticalGradient(
    colors = listOf(
        android.graphics.Color.parseColor("#FFE8E8").let { androidx.compose.ui.graphics.Color(it) },
        android.graphics.Color.parseColor("#F8F8F9").let { androidx.compose.ui.graphics.Color(it) }
    )
)

val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(Primary, PrimaryDark)
)
