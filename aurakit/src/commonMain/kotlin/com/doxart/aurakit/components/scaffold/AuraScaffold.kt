package com.doxart.aurakit.components.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.doxart.aurakit.components.appbar.AuraTopAppBar
import com.doxart.aurakit.components.appbar.AuraTopAppBarDefaults
import com.doxart.aurakit.components.appbar.AuraTopAppBarStyle
import com.doxart.aurakit.components.navigation.AuraBottomBarColors
import com.doxart.aurakit.components.navigation.AuraBottomBarDefaults
import com.doxart.aurakit.components.navigation.AuraBottomBarItem
import com.doxart.aurakit.components.navigation.AuraBottomBarStyle
import com.doxart.aurakit.components.navigation.AuraExpandableBottomBar
import com.doxart.aurakit.theme.AuraTheme

/**
 * Centralized, immersive Scaffold adhering faithfully to the easywallet `ViewController` architecture.
 *
 * Architecture Highlights:
 * 1. **Floating & Overlay Layering**: Content extends to the entire screen bounds (edge-to-edge).
 *    TopBar and BottomBar float on top of the content layer via Box z-ordering.
 * 2. **Automatic Bar Clearance**: Content block receives [PaddingValues] that cleanly incorporate
 *    both system WindowInsets (statusBars, navigationBars, cutouts) AND bar clearance heights so
 *    scrollable items are never obscured beneath floating elements.
 * 3. **Ready Out-of-the-Box**: Renders standard [AuraTopAppBar] and [AuraExpandableBottomBar]
 *    by default. The caller only needs to supply [content].
 * 4. **Flexible Overrides**: Any bar or FAB can be individually overridden via composable slots,
 *    or hidden entirely with [showTopBar] and [showBottomBar].
 * 5. **Integrated Nested Scrolling**: Automatically drives bottom bar collapse/expand state
 *    during scroll interactions.
 *
 * @param modifier Root modifier for the scaffold container.
 * @param state State holder managing bar expansion, active tab, and nested scroll connection.
 * @param showTopBar When true, the top bar is rendered and top padding includes its clearance.
 * @param topBar Custom composable slot to replace the default [AuraTopAppBar].
 * @param titleText Plain text title used by the default top app bar.
 * @param topBarTitle Custom composable title used by the default top app bar.
 * @param topBarStyle Visual style for the default top app bar (defaults to [AuraTopAppBarStyle.Gradient]).
 * @param topBarNavigationIcon Custom navigation icon for the default top app bar.
 * @param onTopBarNavigationClick Callback for navigation icon tap on default top app bar.
 * @param topBarActions Action items slot for default top app bar (e.g., [com.doxart.aurakit.components.appbar.AuraAppBarActionGroup]).
 * @param showBottomBar When true, the bottom bar is rendered and bottom padding includes its clearance.
 * @param bottomBar Custom composable slot to replace the default [AuraExpandableBottomBar].
 * @param bottomBarItems Navigation items for default expandable bottom bar.
 * @param selectedBottomBarIndex Currently active tab index for the bottom bar.
 * @param onBottomBarItemSelected Callback invoked when a tab in the default bottom bar is selected.
 * @param onBottomBarActionClick Callback invoked when the trailing action button on bottom bar is clicked.
 * @param bottomBarSearchValue Search input value for bottom bar search mode.
 * @param onBottomBarSearchValueChange Callback for search text updates.
 * @param bottomBarSearchPlaceholder Placeholder label for search field.
 * @param snackbarHost Optional slot for snackbar display, floating above bottom bar.
 * @param floatingActionButton Optional floating action button slot.
 * @param floatingActionButtonPosition Screen alignment for [floatingActionButton].
 * @param topBarHeight Height metric allocated for top bar clearance calculation.
 * @param bottomBarHeight Height metric allocated for bottom bar clearance calculation.
 * @param enableNestedScroll Enables auto-collapse and auto-expand of bottom bar on scroll.
 * @param colors Background and overlay color specifications.
 * @param content Primary screen content receiving calculated [PaddingValues].
 */
@Composable
fun AuraScaffold(
    modifier: Modifier = Modifier,
    state: AuraScaffoldState = rememberAuraScaffoldState(),
    showTopBar: Boolean = true,
    topBar: (@Composable () -> Unit)? = null,
    topBarScrimModifier: Modifier? = null,
    titleText: String? = "AuraKit",
    topBarTitle: (@Composable () -> Unit)? = null,
    topBarStyle: AuraTopAppBarStyle = AuraTopAppBarStyle.Gradient,
    topBarNavigationIcon: (@Composable () -> Unit)? = null,
    onTopBarNavigationClick: (() -> Unit)? = null,
    topBarActions: (@Composable RowScope.() -> Unit)? = null,
    showTopBarScrim: Boolean = true,
    showBottomBar: Boolean = true,
    bottomBar: (@Composable () -> Unit)? = null,
    bottomBarStyle: AuraBottomBarStyle = AuraBottomBarStyle.Elevated,
    bottomBarColors: AuraBottomBarColors = AuraBottomBarDefaults.colors(style = bottomBarStyle),
    showBottomBarScrim: Boolean = true,
    enableBottomBarItemRipple: Boolean = false,
    bottomBarItems: List<AuraBottomBarItem<*>> = AuraScaffoldDefaults.defaultBottomBarItems(),
    selectedBottomBarIndex: Int = state.selectedBottomBarIndex,
    onBottomBarItemSelected: ((index: Int, item: AuraBottomBarItem<*>) -> Unit)? = null,
    onBottomBarActionClick: (() -> Unit)? = null,
    bottomBarSearchValue: String = "",
    onBottomBarSearchValueChange: (String) -> Unit = {},
    bottomBarSearchPlaceholder: String = "Search...",
    snackbarHost: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: AuraFabPosition = AuraFabPosition.End,
    topBarHeight: Dp = AuraScaffoldDefaults.TopBarHeight,
    bottomBarHeight: Dp = AuraScaffoldDefaults.BottomBarHeight,
    enableNestedScroll: Boolean = true,
    colors: AuraScaffoldColors = AuraScaffoldDefaults.colors(),
    content: @Composable (PaddingValues) -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current
    val safeDrawing = WindowInsets.safeDrawing.asPaddingValues()
    val statusBarsTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarsBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val topClearance = if (showTopBar) {
        statusBarsTop + topBarHeight
    } else {
        statusBarsTop
    }

    val bottomClearance = if (showBottomBar) {
        navBarsBottom + bottomBarHeight + AuraScaffoldDefaults.FloatingBarSpacing
    } else {
        navBarsBottom
    }

    val contentPadding = PaddingValues(
        start = safeDrawing.calculateStartPadding(layoutDirection),
        top = topClearance,
        end = safeDrawing.calculateEndPadding(layoutDirection),
        bottom = bottomClearance
    )

    CompositionLocalProvider(LocalAuraScaffoldState provides state) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(colors.containerColor)
                .then(
                    if (enableNestedScroll) {
                        Modifier.nestedScroll(state.nestedScrollConnection)
                    } else Modifier
                )
        ) {
            // LAYER 0: CONTENT (Full-bleed edge-to-edge behind floating bars)
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                content(contentPadding)
            }

            // LAYER 1: FLOATING TOP APP BAR (Aligned to top)
            if (showTopBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .zIndex(1f)
                ) {
                    if (topBar != null) {
                        if (showTopBarScrim) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .matchParentSize()
                                    .background(AuraScaffoldDefaults.defaultTopScrimBrush())
                            )
                        }
                        topBar()
                    } else {
                        Box {
                            if (topBarScrimModifier != null) {
                                Box(
                                    topBarScrimModifier
                                        .fillMaxWidth()
                                        .matchParentSize()
                                )
                            }
                            AuraTopAppBar(
                                title = topBarTitle,
                                titleText = titleText,
                                style = topBarStyle,
                                showScrim = showTopBarScrim,
                                navigationIcon = topBarNavigationIcon,
                                onNavigationClick = onTopBarNavigationClick,
                                actions = topBarActions
                            )
                        }
                    }
                }
            }

            // LAYER 2: SNACKBAR HOST (Floating above bottom bar)
            if (snackbarHost != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(
                            bottom = if (showBottomBar) {
                                navBarsBottom + bottomBarHeight + AuraTheme.spacing.md
                            } else {
                                navBarsBottom + AuraTheme.spacing.md
                            }
                        )
                        .zIndex(2f)
                ) {
                    snackbarHost()
                }
            }

            // LAYER 3: FLOATING ACTION BUTTON (Positioned cleanly above bottom bar)
            if (floatingActionButton != null) {
                val fabAlignment = when (floatingActionButtonPosition) {
                    AuraFabPosition.Start -> Alignment.BottomStart
                    AuraFabPosition.Center -> Alignment.BottomCenter
                    AuraFabPosition.End -> Alignment.BottomEnd
                }
                val bottomInsetDp = WindowInsets.navigationBars.union(WindowInsets.ime).asPaddingValues().calculateBottomPadding()
                val fabBottomPadding = if (showBottomBar) {
                    bottomInsetDp + bottomBarHeight + AuraTheme.spacing.lg
                } else {
                    bottomInsetDp + AuraTheme.spacing.lg
                }

                Box(
                    modifier = Modifier
                        .align(fabAlignment)
                        .padding(
                            bottom = fabBottomPadding,
                            start = if (floatingActionButtonPosition == AuraFabPosition.Start) AuraTheme.spacing.lg else AuraTheme.spacing.none,
                            end = if (floatingActionButtonPosition == AuraFabPosition.End) AuraTheme.spacing.lg else AuraTheme.spacing.none
                        )
                        .zIndex(3f)
                ) {
                    floatingActionButton()
                }
            }

            // LAYER 4: FLOATING EXPANDABLE BOTTOM BAR (Aligned to bottom)
            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                ) {
                    if (bottomBar != null) {
                        if (showBottomBarScrim) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .matchParentSize()
                                    .background(AuraScaffoldDefaults.defaultBottomScrimBrush())
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                        ) {
                            bottomBar()
                        }
                    } else {
                        @Suppress("UNCHECKED_CAST")
                        AuraExpandableBottomBar(
                            windowInsets = WindowInsets.navigationBars.union(WindowInsets.ime),
                            items = bottomBarItems as List<AuraBottomBarItem<Any?>>,
                            selectedIndex = selectedBottomBarIndex,
                            onItemSelected = { index, item ->
                                state.selectedBottomBarIndex = index
                                onBottomBarItemSelected?.invoke(index, item)
                            },
                            style = bottomBarStyle,
                            colors = bottomBarColors,
                            showScrim = showBottomBarScrim,
                            enableItemRipple = enableBottomBarItemRipple,
                            isExpanded = state.isBottomBarExpanded,
                            onToggleExpand = { state.toggleBottomBarExpand() },
                            isExtra = state.isBottomBarExtra,
                            onToggleExtra = { state.updateBottomBarExtra(it) },
                            searchValue = bottomBarSearchValue,
                            onSearchValueChange = onBottomBarSearchValueChange,
                            searchPlaceholder = bottomBarSearchPlaceholder,
                            onTyping = {
                                state.isTyping = it
                                if (it) state.expandBottomBar()
                            },
                            onActionClick = {
                                if (onBottomBarActionClick != null) {
                                    onBottomBarActionClick()
                                } else {
                                    state.toggleBottomBarExtra()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Direct alias for [AuraScaffold], fulfilling the project's [AppScaffold] naming convention.
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    state: AuraScaffoldState = rememberAuraScaffoldState(),
    showTopBar: Boolean = true,
    topBar: (@Composable () -> Unit)? = null,
    titleText: String? = "AuraKit",
    topBarTitle: (@Composable () -> Unit)? = null,
    topBarStyle: AuraTopAppBarStyle = AuraTopAppBarStyle.Gradient,
    topBarNavigationIcon: (@Composable () -> Unit)? = null,
    onTopBarNavigationClick: (() -> Unit)? = null,
    topBarActions: (@Composable RowScope.() -> Unit)? = null,
    showTopBarScrim: Boolean = true,
    showBottomBar: Boolean = true,
    bottomBar: (@Composable () -> Unit)? = null,
    bottomBarStyle: AuraBottomBarStyle = AuraBottomBarStyle.Elevated,
    bottomBarColors: AuraBottomBarColors = AuraBottomBarDefaults.colors(style = bottomBarStyle),
    showBottomBarScrim: Boolean = true,
    enableBottomBarItemRipple: Boolean = false,
    bottomBarItems: List<AuraBottomBarItem<*>> = AuraScaffoldDefaults.defaultBottomBarItems(),
    selectedBottomBarIndex: Int = state.selectedBottomBarIndex,
    onBottomBarItemSelected: ((index: Int, item: AuraBottomBarItem<*>) -> Unit)? = null,
    onBottomBarActionClick: (() -> Unit)? = null,
    bottomBarSearchValue: String = "",
    onBottomBarSearchValueChange: (String) -> Unit = {},
    bottomBarSearchPlaceholder: String = "Search...",
    snackbarHost: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: AuraFabPosition = AuraFabPosition.End,
    topBarHeight: Dp = AuraScaffoldDefaults.TopBarHeight,
    bottomBarHeight: Dp = AuraScaffoldDefaults.BottomBarHeight,
    enableNestedScroll: Boolean = true,
    colors: AuraScaffoldColors = AuraScaffoldDefaults.colors(),
    content: @Composable (PaddingValues) -> Unit
) = AuraScaffold(
    modifier = modifier,
    state = state,
    showTopBar = showTopBar,
    topBar = topBar,
    titleText = titleText,
    topBarTitle = topBarTitle,
    topBarStyle = topBarStyle,
    topBarNavigationIcon = topBarNavigationIcon,
    onTopBarNavigationClick = onTopBarNavigationClick,
    topBarActions = topBarActions,
    showTopBarScrim = showTopBarScrim,
    showBottomBar = showBottomBar,
    bottomBar = bottomBar,
    bottomBarStyle = bottomBarStyle,
    bottomBarColors = bottomBarColors,
    showBottomBarScrim = showBottomBarScrim,
    enableBottomBarItemRipple = enableBottomBarItemRipple,
    bottomBarItems = bottomBarItems,
    selectedBottomBarIndex = selectedBottomBarIndex,
    onBottomBarItemSelected = onBottomBarItemSelected,
    onBottomBarActionClick = onBottomBarActionClick,
    bottomBarSearchValue = bottomBarSearchValue,
    onBottomBarSearchValueChange = onBottomBarSearchValueChange,
    bottomBarSearchPlaceholder = bottomBarSearchPlaceholder,
    snackbarHost = snackbarHost,
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    topBarHeight = topBarHeight,
    bottomBarHeight = bottomBarHeight,
    enableNestedScroll = enableNestedScroll,
    colors = colors,
    content = content
)

/**
 * Direct alias for [AuraScaffold], fulfilling the project's [MainScaffold] naming convention.
 */
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier,
    state: AuraScaffoldState = rememberAuraScaffoldState(),
    showTopBar: Boolean = true,
    topBar: (@Composable () -> Unit)? = null,
    titleText: String? = "AuraKit",
    topBarTitle: (@Composable () -> Unit)? = null,
    topBarStyle: AuraTopAppBarStyle = AuraTopAppBarStyle.Gradient,
    topBarNavigationIcon: (@Composable () -> Unit)? = null,
    onTopBarNavigationClick: (() -> Unit)? = null,
    topBarActions: (@Composable RowScope.() -> Unit)? = null,
    showTopBarScrim: Boolean = true,
    showBottomBar: Boolean = true,
    bottomBar: (@Composable () -> Unit)? = null,
    bottomBarStyle: AuraBottomBarStyle = AuraBottomBarStyle.Elevated,
    bottomBarColors: AuraBottomBarColors = AuraBottomBarDefaults.colors(style = bottomBarStyle),
    showBottomBarScrim: Boolean = true,
    enableBottomBarItemRipple: Boolean = false,
    bottomBarItems: List<AuraBottomBarItem<*>> = AuraScaffoldDefaults.defaultBottomBarItems(),
    selectedBottomBarIndex: Int = state.selectedBottomBarIndex,
    onBottomBarItemSelected: ((index: Int, item: AuraBottomBarItem<*>) -> Unit)? = null,
    onBottomBarActionClick: (() -> Unit)? = null,
    bottomBarSearchValue: String = "",
    onBottomBarSearchValueChange: (String) -> Unit = {},
    bottomBarSearchPlaceholder: String = "Search...",
    snackbarHost: (@Composable () -> Unit)? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: AuraFabPosition = AuraFabPosition.End,
    topBarHeight: Dp = AuraScaffoldDefaults.TopBarHeight,
    bottomBarHeight: Dp = AuraScaffoldDefaults.BottomBarHeight,
    enableNestedScroll: Boolean = true,
    colors: AuraScaffoldColors = AuraScaffoldDefaults.colors(),
    content: @Composable (PaddingValues) -> Unit
) = AuraScaffold(
    modifier = modifier,
    state = state,
    showTopBar = showTopBar,
    topBar = topBar,
    titleText = titleText,
    topBarTitle = topBarTitle,
    topBarStyle = topBarStyle,
    topBarNavigationIcon = topBarNavigationIcon,
    onTopBarNavigationClick = onTopBarNavigationClick,
    topBarActions = topBarActions,
    showTopBarScrim = showTopBarScrim,
    showBottomBar = showBottomBar,
    bottomBar = bottomBar,
    bottomBarStyle = bottomBarStyle,
    bottomBarColors = bottomBarColors,
    showBottomBarScrim = showBottomBarScrim,
    enableBottomBarItemRipple = enableBottomBarItemRipple,
    bottomBarItems = bottomBarItems,
    selectedBottomBarIndex = selectedBottomBarIndex,
    onBottomBarItemSelected = onBottomBarItemSelected,
    onBottomBarActionClick = onBottomBarActionClick,
    bottomBarSearchValue = bottomBarSearchValue,
    onBottomBarSearchValueChange = onBottomBarSearchValueChange,
    bottomBarSearchPlaceholder = bottomBarSearchPlaceholder,
    snackbarHost = snackbarHost,
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    topBarHeight = topBarHeight,
    bottomBarHeight = bottomBarHeight,
    enableNestedScroll = enableNestedScroll,
    colors = colors,
    content = content
)

/**
 * Reusable frosted-glass floating bar container for custom bottom bar implementations.
 *
 * @param modifier Modifier for the container.
 * @param shape Corner shape (defaults to [AuraTheme.shapes.pill]).
 * @param containerColor Frosted glass surface color.
 * @param borderColor Subtle outline color.
 * @param content Child content placed horizontally inside the pill.
 */
@Composable
fun AuraFloatingBarContainer(
    modifier: Modifier = Modifier,
    shape: Shape = AuraTheme.shapes.pill,
    containerColor: Color = AuraTheme.colors.surfaceElevated.copy(alpha = 0.92f),
    borderColor: Color = AuraTheme.colors.outlineVariant,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .padding(horizontal = AuraTheme.spacing.lg, vertical = AuraTheme.spacing.xs)
            .clip(shape)
            .border(AuraTheme.sizing.strokeThin, borderColor, shape),
        shape = shape,
        color = containerColor,
        shadowElevation = AuraTheme.spacing.xs
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AuraTheme.spacing.md, vertical = AuraTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}
