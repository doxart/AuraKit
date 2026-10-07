package com.doxart.aurakit.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAuraColors = staticCompositionLocalOf { AuraDarkColors }
val LocalAuraTypography = staticCompositionLocalOf { DefaultAuraTypography }
val LocalAuraSpacing = staticCompositionLocalOf { DefaultAuraSpacing }
val LocalAuraSizing = staticCompositionLocalOf { DefaultAuraSizing }
val LocalAuraShapes = staticCompositionLocalOf { DefaultAuraShapes }
val LocalAuraMotion = staticCompositionLocalOf { DefaultAuraMotion }

object AuraTheme {
    val colors: AuraColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraColors.current

    val typography: AuraTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraTypography.current

    val spacing: AuraSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraSpacing.current

    val sizing: AuraSizing
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraSizing.current

    val shapes: AuraShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraShapes.current

    val motion: AuraMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalAuraMotion.current

    val gradients: AuraGradients
        get() = AuraGradients

    val brand: AuraBrandColors
        get() = AuraBrandColors
}

@Composable
fun AuraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: AuraColors = if (darkTheme) AuraDarkColors else AuraLightColors,
    typography: AuraTypography = DefaultAuraTypography,
    spacing: AuraSpacing = DefaultAuraSpacing,
    sizing: AuraSizing = DefaultAuraSizing,
    shapes: AuraShapes = DefaultAuraShapes,
    motion: AuraMotion = DefaultAuraMotion,
    content: @Composable () -> Unit
) {
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surfaceVariant,
            onSurfaceVariant = colors.onSurfaceVariant,
            outline = colors.outline,
            outlineVariant = colors.outlineVariant,
            error = colors.error,
            onError = colors.onError
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surfaceVariant,
            onSurfaceVariant = colors.onSurfaceVariant,
            outline = colors.outline,
            outlineVariant = colors.outlineVariant,
            error = colors.error,
            onError = colors.onError
        )
    }

    CompositionLocalProvider(
        LocalAuraColors provides colors,
        LocalAuraTypography provides typography,
        LocalAuraSpacing provides spacing,
        LocalAuraSizing provides sizing,
        LocalAuraShapes provides shapes,
        LocalAuraMotion provides motion
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content
        )
    }
}
