package com.doxart.aurakit.components.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.doxart.aurakit.components.appbar.AuraTopAppBarDefaults
import com.doxart.aurakit.components.navigation.AuraBottomBarDefaults
import com.doxart.aurakit.components.navigation.AuraBottomBarItem
import com.doxart.aurakit.foundation.AuraIcons
import com.doxart.aurakit.theme.AuraTheme

/**
 * Default metrics, colors, and standard navigation items for [AuraScaffold].
 */
object AuraScaffoldDefaults {

    /** Default height allocated for TopAppBar clearance (matches sizing barHeightExpanded = 64.dp). */
    val TopBarHeight: Dp
        @Composable
        get() = AuraTheme.sizing.barHeightExpanded

    /** Default height allocated for BottomBar clearance (matches sizing barHeightExpanded = 64.dp). */
    val BottomBarHeight: Dp
        @Composable
        get() = AuraTheme.sizing.barHeightExpanded

    /** Collapsed height of bottom bar (matches sizing barHeightCollapsed = 48.dp). */
    val BottomBarCollapsedHeight: Dp
        @Composable
        get() = AuraTheme.sizing.barHeightCollapsed

    /** Vertical spacing buffer between floating bottom bar and content clearance (16.dp). */
    val FloatingBarSpacing: Dp
        @Composable
        get() = AuraTheme.spacing.base

    /**
     * Default multi-stop vertical gradient scrim brush for top bar (BlackToNone equivalent).
     */
    @Composable
    fun defaultTopScrimBrush(): Brush = AuraTopAppBarDefaults.defaultTopScrimBrush()

    /**
     * Default multi-stop vertical gradient scrim brush for bottom bar (NoneToBlack equivalent).
     */
    @Composable
    fun defaultBottomScrimBrush(): Brush = AuraBottomBarDefaults.defaultBottomScrimBrush()

    /**
     * Resolves default [AuraScaffoldColors] dynamically from the current [AuraTheme.colors].
     */
    @Composable
    fun colors(
        containerColor: Color = AuraTheme.colors.background,
        topBarOverlayColor: Color = Color.Transparent,
        bottomBarOverlayColor: Color = Color.Transparent
    ): AuraScaffoldColors = AuraScaffoldColors(
        containerColor = containerColor,
        topBarOverlayColor = topBarOverlayColor,
        bottomBarOverlayColor = bottomBarOverlayColor
    )

    /**
     * Standard navigation items for out-of-the-box usage with [AuraScaffold].
     * Can be customized by passing a custom item list to [AuraScaffold].
     */
    fun defaultBottomBarItems(): List<AuraBottomBarItem<String>> = listOf(
        AuraBottomBarItem(data = "home", title = "Home", icon = AuraIcons.Home),
        AuraBottomBarItem(data = "wallet", title = "Wallet", icon = AuraIcons.Wallet),
        AuraBottomBarItem(data = "swap", title = "Swap", icon = AuraIcons.Swap),
        AuraBottomBarItem(data = "search", title = "Search", icon = AuraIcons.Search)
    )
}
