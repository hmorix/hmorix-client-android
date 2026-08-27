package com.hmorix.client.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ObsidianBg = Color(0xFF0D0D0D)
val ObsidianElevated = Color(0xFF141414)
val ObsidianCard = Color(0xFF1A1A1A)
val ObsidianBorder = Color(0xFF262626)

val ElectricLime = Color(0xFFC8FF00)
val ElectricLimeDim = Color(0xFF98D600)
val ElectricLimeAlpha10 = Color(0x1AC8FF00)
val ElectricLimeAlpha20 = Color(0x33C8FF00)

val Cream = Color(0xFFEAE8E3)
val CreamMuted = Color(0xFFA3A099)
val CreamSubtle = Color(0xFF666666)

val AccentRed = Color(0xFFFF4D4D)
val AccentGreen = Color(0xFF10B981)
val AccentBlue = Color(0xFF38BDF8)

private val DarkColorScheme = darkColorScheme(
    primary = ElectricLime,
    onPrimary = ObsidianBg,
    primaryContainer = ElectricLimeAlpha20,
    onPrimaryContainer = ElectricLime,
    secondary = Cream,
    onSecondary = ObsidianBg,
    background = ObsidianBg,
    onBackground = Cream,
    surface = ObsidianElevated,
    onSurface = Cream,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = CreamMuted,
    outline = ObsidianBorder,
    error = AccentRed,
    onError = ObsidianBg
)

@Composable
fun HMorixTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
