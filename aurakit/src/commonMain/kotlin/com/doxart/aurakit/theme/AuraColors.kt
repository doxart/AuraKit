package com.doxart.aurakit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * AuraKit Brand & Web Palette Tokens.
 * Directly synchronized with the AuraKit Web Design System (`C:\Projects\WebSources\AuraKit`).
 */
object AuraBrandColors {
    // Signature Accents
    val Accent = Color(0xFF9292FF)       // --color-accent (Periwinkle / Soft Iris)
    val Lime = Color(0xFFF4FF8B)         // --color-1 (Neon Electric Lime)
    val Violet = Color(0xFFA176FF)       // --color-2 (Electric Violet)
    val Cyan = Color(0xFF06BDFE)         // --color-3 (Electric Sky Cyan)
    val Orange = Color(0xFFFF6C22)       // --color-4 (Tangerine Flame)
    val SunsetOrange = Color(0xFFFF8A00) // Roadmap / burst stroke

    // Canvas & Containers (Navy / Dark Obsidian)
    val CanvasDark = Color(0xFF0D0D0D)   // --bg-main
    val CanvasBlack = Color(0xFF040A12)  // Deep Midnight Canvas
    val Navy = Color(0xFF0F2340)          // --bg-sidebar / --bg-banner
    val NavyDeep = Color(0xFF0B1524)      // Code block background
    val NavySurface = Color(0xFF173053)   // Card hover / elevated navy

    // Dialog & Feedback Specific Surfaces
    val DatePickerSurface = Color(0xFF161622)
    val LoadingDialogSurface = Color(0xFF1F2115)
    val AnswerDialogSurface = Color(0xFF0D1720)
    val SnackbarBg = Color(0xFF6C63FF)
    val SnackbarText = Color(0xFFF4FF8B)

    // Wallet Illustration
    val WalletCardPink = Color(0xFFF43F5E)
    val WalletCardAmber = Color(0xFFFBBF24)
    val WalletCardSlate = Color(0xFF1E293B)

    // Text & Muted
    val TextMain = Color(0xFFFFFFFF)     // --text-main
    val TextMuted = Color(0xFF9CA3AF)    // --text-muted
    val TextDark = Color(0xFF0F172A)
}

/**
 * Multi-stop Gradient Brushes ported from AuraKit Web (`GetStarted.css` & `Banner.css`).
 */
object AuraGradients {
    val Implementation = Brush.linearGradient(
        colors = listOf(Color(0xFF06BDFE), Color(0xFF00C9FF), Color(0xFF845EC2))
    )
    val Components = Brush.linearGradient(
        colors = listOf(Color(0xFFF3FE89), Color(0xFF91A00F), Color(0xFF00C9FF), Color(0xFFF3FE89))
    )
    val Future = Brush.linearGradient(
        colors = listOf(Color(0xFF2B005E), Color(0xFF710080), Color(0xFFFF8A00))
    )
    val Title = Brush.linearGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFB8B8FF))
    )
    val NavyCard = Brush.linearGradient(
        colors = listOf(Color(0xFF0F2340), Color(0xFF0F2340))
    )
    val Shield = Brush.linearGradient(
        colors = listOf(Color(0x33FFFFFF), Color(0x00FFFFFF))
    )
}

@Immutable
data class AuraColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val accent: Color,
    val onAccent: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceElevated: Color,
    val outline: Color,
    val outlineVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val isDark: Boolean,
    val brandAccent: Color = AuraBrandColors.Accent,
    val brandLime: Color = AuraBrandColors.Lime,
    val brandViolet: Color = AuraBrandColors.Violet,
    val brandCyan: Color = AuraBrandColors.Cyan,
    val brandOrange: Color = AuraBrandColors.Orange,
    val brandNavy: Color = AuraBrandColors.Navy,
    val brandNavyDeep: Color = AuraBrandColors.NavyDeep
)

val AuraDarkColors = AuraColors(
    primary = AuraBrandColors.Accent,             // #9292FF (Web --color-accent)
    onPrimary = AuraBrandColors.CanvasBlack,       // #040A12 (Web canvas dark)
    primaryContainer = AuraBrandColors.Navy,       // #0F2340 (Web --bg-sidebar)
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = AuraBrandColors.Cyan,             // #06BDFE (Web --color-3)
    onSecondary = Color(0xFF083344),
    secondaryContainer = Color(0xFF164E63),
    onSecondaryContainer = Color(0xFFCFFAFE),
    accent = AuraBrandColors.Violet,              // #A176FF (Web --color-2)
    onAccent = Color(0xFF3B0764),
    background = AuraBrandColors.CanvasDark,      // #0D0D0D (Web --bg-main)
    onBackground = AuraBrandColors.TextMain,      // #FFFFFF (Web --text-main)
    surface = AuraBrandColors.Navy,               // #0F2340 (Web Navy surface)
    onSurface = AuraBrandColors.TextMain,
    surfaceVariant = AuraBrandColors.DatePickerSurface, // #161622
    onSurfaceVariant = Color(0xFFCBD5E1),
    surfaceElevated = AuraBrandColors.WalletCardSlate,  // #1E293B
    outline = Color(0xFF253348),
    outlineVariant = Color(0xFF1B283A),
    textPrimary = AuraBrandColors.TextMain,
    textSecondary = AuraBrandColors.TextMuted,    // #9CA3AF (Web --text-muted)
    textMuted = Color(0xFF6B7280),
    success = Color(0xFF10B981),
    onSuccess = Color(0xFF022C22),
    successContainer = Color(0xFF064E3B),
    warning = AuraBrandColors.Orange,             // #FF6C22 (Web --color-4)
    onWarning = Color(0xFF451A03),
    warningContainer = Color(0xFF78350F),
    error = Color(0xFFEF4444),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    info = AuraBrandColors.Cyan,                  // #06BDFE (Web --color-3)
    onInfo = Color(0xFF082F49),
    infoContainer = Color(0xFF075985),
    isDark = true,
    brandAccent = AuraBrandColors.Accent,
    brandLime = AuraBrandColors.Lime,
    brandViolet = AuraBrandColors.Violet,
    brandCyan = AuraBrandColors.Cyan,
    brandOrange = AuraBrandColors.Orange,
    brandNavy = AuraBrandColors.Navy,
    brandNavyDeep = AuraBrandColors.NavyDeep
)

val AuraLightColors = AuraColors(
    primary = Color(0xFF5B5BF0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEEF2FF),
    onPrimaryContainer = Color(0xFF312E81),
    secondary = Color(0xFF0284C7),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFECFEFF),
    onSecondaryContainer = Color(0xFF164E63),
    accent = Color(0xFF8B5CF6),
    onAccent = Color(0xFFFFFFFF),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF334155),
    surfaceElevated = Color(0xFFFFFFFF),
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFCBD5E1),
    textPrimary = Color(0xFF0F172A),
    textSecondary = Color(0xFF475569),
    textMuted = Color(0xFF94A3B8),
    success = Color(0xFF059669),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFECFDF5),
    warning = Color(0xFFD97706),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFFBEB),
    error = Color(0xFFDC2626),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEF2F2),
    info = Color(0xFF0284C7),
    onInfo = Color(0xFFFFFFFF),
    infoContainer = Color(0xFFF0F9FF),
    isDark = false,
    brandAccent = AuraBrandColors.Accent,
    brandLime = AuraBrandColors.Lime,
    brandViolet = AuraBrandColors.Violet,
    brandCyan = AuraBrandColors.Cyan,
    brandOrange = AuraBrandColors.Orange,
    brandNavy = AuraBrandColors.Navy,
    brandNavyDeep = AuraBrandColors.NavyDeep
)

