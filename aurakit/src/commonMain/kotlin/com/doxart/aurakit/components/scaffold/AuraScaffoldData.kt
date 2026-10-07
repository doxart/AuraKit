package com.doxart.aurakit.components.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

/**
 * Alignment positions for floating action buttons inside [AuraScaffold].
 */
enum class AuraFabPosition {
    Start,
    Center,
    End
}

/**
 * Styling and surface colors for [AuraScaffold].
 */
@Stable
data class AuraScaffoldColors(
    val containerColor: Color,
    val topBarOverlayColor: Color = Color.Transparent,
    val bottomBarOverlayColor: Color = Color.Transparent
)

/**
 * State holder for [AuraScaffold], managing bottom bar collapse/expand,
 * action/search mode, active tab selection, and nested scrolling interactions.
 *
 * Faithfully mirrors easywallet's [ViewController] interaction engine.
 */
@Stable
class AuraScaffoldState(
    initialTabIndex: Int = 0,
    initialExpanded: Boolean = true,
    initialExtra: Boolean = false
) {
    var selectedBottomBarIndex by mutableIntStateOf(initialTabIndex)
    var isBottomBarExpanded by mutableStateOf(initialExpanded)
    var isBottomBarExtra by mutableStateOf(initialExtra)
    var isTyping by mutableStateOf(false)

    /**
     * Nested scroll connection that collapses the floating bottom bar when
     * scrolling down (negative dy < -25f) and expands it when scrolling up (positive dy > 25f),
     * matching the exact threshold in easywallet's ViewController.
     */
    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (available.y < -25f && isBottomBarExpanded) {
                isBottomBarExpanded = if (isBottomBarExtra) isTyping else false
            } else if (available.y > 25f && !isBottomBarExpanded) {
                isBottomBarExpanded = true
            }
            return Offset.Zero
        }
    }

    fun expandBottomBar() {
        isBottomBarExpanded = true
    }

    fun collapseBottomBar() {
        isBottomBarExpanded = false
    }

    fun toggleBottomBarExpand() {
        isBottomBarExpanded = !isBottomBarExpanded
    }

    fun updateBottomBarExtra(extra: Boolean) {
        isBottomBarExtra = extra
        if (extra) {
            isBottomBarExpanded = true
        }
    }

    fun toggleBottomBarExtra() {
        updateBottomBarExtra(!isBottomBarExtra)
    }
}

/**
 * CompositionLocal providing access to the nearest [AuraScaffoldState].
 */
val LocalAuraScaffoldState = compositionLocalOf<AuraScaffoldState?> { null }

/**
 * Creates and remembers an [AuraScaffoldState] across recompositions.
 */
@Composable
fun rememberAuraScaffoldState(
    initialTabIndex: Int = 0,
    initialExpanded: Boolean = true,
    initialExtra: Boolean = false
): AuraScaffoldState = remember {
    AuraScaffoldState(
        initialTabIndex = initialTabIndex,
        initialExpanded = initialExpanded,
        initialExtra = initialExtra
    )
}
