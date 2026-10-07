package com.doxart.aurakit.components.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.doxart.aurakit.foundation.AuraIcons
import com.doxart.aurakit.theme.AuraTheme

@Immutable
data class AuraBottomBarItem<T>(
    val data: T,
    val title: String,
    val icon: ImageVector? = null,
    val selectedIcon: ImageVector? = null,
    val customIcon: (@Composable (isSelected: Boolean) -> Unit)? = null
)

/**
 * Visual background style variants for [AuraExpandableBottomBar].
 */
enum class AuraBottomBarStyle {
    /**
     * Completely transparent pill container with soft multi-stop bottom scrim underneath.
     * Content scrolling underneath is smoothly faded by the scrim while items and icons remain sharp.
     */
    Transparent,

    /**
     * Standard elevated translucent container with subtle border and soft multi-stop bottom scrim.
     */
    Elevated,

    /**
     * Solid opaque surface matching the current theme canvas.
     */
    Solid
}

@Immutable
data class AuraBottomBarColors(
    val containerColor: Color,
    val borderColor: Color,
    val activeIndicatorColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val actionContainerColor: Color,
    val actionContentColor: Color,
    val searchTextColor: Color,
    val searchPlaceholderColor: Color,
    val scrimBrush: Brush? = null
)

/**
 * Default metrics, styles, and color factory for [AuraExpandableBottomBar].
 */
object AuraBottomBarDefaults {

    /**
     * Default multi-stop vertical gradient scrim for bottom bars.
     * Matches the easywallet `NoneToBlack` brush, providing optical smooth fade from
     * transparent at the top to the solid theme background at the bottom.
     */
    @Composable
    fun defaultBottomScrimBrush(
        startColor: Color = Color.Transparent,
        endColor: Color = AuraTheme.colors.background
    ): Brush = Brush.verticalGradient(
        colors = listOf(
            startColor,
            endColor.copy(alpha = 0.25f),
            endColor.copy(alpha = 0.55f),
            endColor.copy(alpha = 0.85f),
            endColor
        )
    )

    /**
     * Standard colors specification for [AuraExpandableBottomBar], resolving dynamically
     * based on [AuraBottomBarStyle] and [AuraTheme.colors].
     */
    @Composable
    fun colors(
        style: AuraBottomBarStyle = AuraBottomBarStyle.Elevated,
        containerColor: Color = when (style) {
            AuraBottomBarStyle.Transparent -> Color.Transparent
            AuraBottomBarStyle.Elevated -> AuraTheme.colors.surfaceElevated.copy(alpha = 0.92f)
            AuraBottomBarStyle.Solid -> AuraTheme.colors.surfaceElevated
        },
        borderColor: Color = when (style) {
            AuraBottomBarStyle.Transparent -> AuraTheme.colors.outlineVariant.copy(alpha = 0.40f)
            else -> AuraTheme.colors.outlineVariant
        },
        activeIndicatorColor: Color = when (style) {
            AuraBottomBarStyle.Transparent -> AuraTheme.colors.primary.copy(alpha = 0.20f)
            else -> AuraTheme.colors.primary.copy(alpha = 0.14f)
        },
        selectedItemColor: Color = AuraTheme.colors.primary,
        unselectedItemColor: Color = AuraTheme.colors.textSecondary,
        actionContainerColor: Color = AuraTheme.colors.primary,
        actionContentColor: Color = AuraTheme.colors.onPrimary,
        searchTextColor: Color = AuraTheme.colors.textPrimary,
        searchPlaceholderColor: Color = AuraTheme.colors.textMuted,
        scrimBrush: Brush? = when (style) {
            AuraBottomBarStyle.Transparent,
            AuraBottomBarStyle.Elevated -> defaultBottomScrimBrush()
            AuraBottomBarStyle.Solid -> null
        }
    ): AuraBottomBarColors = AuraBottomBarColors(
        containerColor = containerColor,
        borderColor = borderColor,
        activeIndicatorColor = activeIndicatorColor,
        selectedItemColor = selectedItemColor,
        unselectedItemColor = unselectedItemColor,
        actionContainerColor = actionContainerColor,
        actionContentColor = actionContentColor,
        searchTextColor = searchTextColor,
        searchPlaceholderColor = searchPlaceholderColor,
        scrimBrush = scrimBrush
    )

    /**
     * Dedicated colors preset for transparent glassmorphism bottom bar state.
     * Applies a transparent container with the [defaultBottomScrimBrush] underneath
     * to keep text and icons readable across all scrolling views.
     */
    @Composable
    fun transparentColors(
        borderColor: Color = AuraTheme.colors.outlineVariant.copy(alpha = 0.40f),
        activeIndicatorColor: Color = AuraTheme.colors.primary.copy(alpha = 0.20f),
        selectedItemColor: Color = AuraTheme.colors.primary,
        unselectedItemColor: Color = AuraTheme.colors.textSecondary,
        actionContainerColor: Color = AuraTheme.colors.primary,
        actionContentColor: Color = AuraTheme.colors.onPrimary,
        searchTextColor: Color = AuraTheme.colors.textPrimary,
        searchPlaceholderColor: Color = AuraTheme.colors.textMuted,
        scrimBrush: Brush? = defaultBottomScrimBrush()
    ): AuraBottomBarColors = colors(
        style = AuraBottomBarStyle.Transparent,
        borderColor = borderColor,
        activeIndicatorColor = activeIndicatorColor,
        selectedItemColor = selectedItemColor,
        unselectedItemColor = unselectedItemColor,
        actionContainerColor = actionContainerColor,
        actionContentColor = actionContentColor,
        searchTextColor = searchTextColor,
        searchPlaceholderColor = searchPlaceholderColor,
        scrimBrush = scrimBrush
    )
}

@Composable
fun <T> AuraExpandableBottomBar(
    items: List<AuraBottomBarItem<T>>,
    selectedIndex: Int,
    onItemSelected: (index: Int, item: AuraBottomBarItem<T>) -> Unit,
    modifier: Modifier = Modifier,
    style: AuraBottomBarStyle = AuraBottomBarStyle.Elevated,
    isExpanded: Boolean = true,
    onToggleExpand: () -> Unit = {},
    isExtra: Boolean = false,
    onToggleExtra: (Boolean) -> Unit = {},
    searchValue: String = "",
    onSearchValueChange: (String) -> Unit = {},
    searchPlaceholder: String = "Search...",
    onTyping: (Boolean) -> Unit = {},
    onSearchDone: (() -> Unit)? = null,
    shape: Shape = AuraTheme.shapes.pill,
    colors: AuraBottomBarColors = AuraBottomBarDefaults.colors(style = style),
    showScrim: Boolean = true,
    enableItemRipple: Boolean = false,
    surfaceModifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.navigationBars.union(WindowInsets.ime),
    actionIcon: (@Composable (isExtra: Boolean) -> Unit)? = null,
    onActionClick: () -> Unit = { onToggleExtra(!isExtra) }
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    val itemOffsets = remember { mutableStateMapOf<Int, Dp>() }
    val itemWidths = remember { mutableStateMapOf<Int, Dp>() }

    val currentHeight by animateDpAsState(
        targetValue = if (isExpanded) AuraTheme.sizing.barHeightExpanded else AuraTheme.sizing.barHeightCollapsed,
        animationSpec = AuraTheme.motion.dpSpringSnappy,
        label = "AuraBottomBarHeight"
    )

    val isLeftExpanded = !isExtra && isExpanded

    val rawLeftWeight by animateFloatAsState(
        targetValue = if (isLeftExpanded) 1f else 0.05f,
        animationSpec = AuraTheme.motion.springSnappy,
        label = "AuraBottomBarLeftWeight"
    )
    val rawRightWeight by animateFloatAsState(
        targetValue = if (isExtra) 1f else 0.05f,
        animationSpec = AuraTheme.motion.springSnappy,
        label = "AuraBottomBarRightWeight"
    )

    val leftWeight = rawLeftWeight.coerceAtLeast(0.001f)
    val rightWeight = rawRightWeight.coerceAtLeast(0.001f)

    val targetOffset = itemOffsets[selectedIndex] ?: AuraTheme.spacing.none
    val targetWidth = itemWidths[selectedIndex] ?: AuraTheme.spacing.none

    val animatedIndicatorOffset by animateDpAsState(
        targetValue = targetOffset,
        animationSpec = AuraTheme.motion.dpSpringSnappy,
        label = "AuraBottomBarIndicatorOffset"
    )
    val animatedIndicatorWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = AuraTheme.motion.dpSpringSnappy,
        label = "AuraBottomBarIndicatorWidth"
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Gradient Scrim Background (NoneToBlack equivalent)
        if (showScrim && colors.scrimBrush != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .matchParentSize()
                    .background(colors.scrimBrush)
            )
        }

        // Main Bar Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .padding(
                    horizontal = AuraTheme.spacing.lg,
                    vertical = AuraTheme.spacing.xs
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (!isExpanded && !isExtra) {
                    Arrangement.SpaceBetween
                } else {
                    Arrangement.spacedBy(AuraTheme.spacing.sm)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT SEGMENT (Tabs / Active Tab / Return Button)
                Surface(
                    modifier = Modifier
                        .height(currentHeight)
                        .then(
                            if (isLeftExpanded) Modifier.weight(leftWeight) else Modifier.width(currentHeight)
                        )
                        .clip(shape)
                        .border(
                            BorderStroke(AuraTheme.sizing.strokeThin, colors.borderColor),
                            shape
                        )
                        .then(surfaceModifier),
                    shape = shape,
                    color = colors.containerColor
                ) {
                    AnimatedContent(
                        targetState = when {
                            isExtra -> 0
                            !isExpanded -> 1
                            else -> 2
                        },
                        transitionSpec = {
                            (fadeIn(tween(200)) + scaleIn(initialScale = 0.92f, animationSpec = tween(200)))
                                .togetherWith(fadeOut(tween(150)) + scaleOut(targetScale = 0.92f, animationSpec = tween(150)))
                        },
                        label = "AuraBottomBarLeftSegment"
                    ) { state ->
                        when (state) {
                            0 -> {
                                // Extra active: Return to tabs button
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = if (enableItemRipple) ripple(color = colors.selectedItemColor.copy(alpha = 0.2f)) else null
                                        ) {
                                            onToggleExtra(false)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = AuraIcons.ArrowBack,
                                        contentDescription = "Back to Tabs",
                                        tint = colors.unselectedItemColor,
                                        modifier = Modifier.size(AuraTheme.sizing.iconXl)
                                    )
                                }
                            }
                            1 -> {
                                // Collapsed: Show only current active tab icon
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = if (enableItemRipple) ripple(color = colors.selectedItemColor.copy(alpha = 0.2f)) else null
                                        ) { onToggleExpand() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    val currentItem = items.getOrNull(selectedIndex) ?: items.firstOrNull()
                                    if (currentItem != null) {
                                        if (currentItem.customIcon != null) {
                                            currentItem.customIcon(true)
                                        } else {
                                            val icon = currentItem.selectedIcon ?: currentItem.icon ?: AuraIcons.Home
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = currentItem.title,
                                                tint = colors.selectedItemColor,
                                                modifier = Modifier.size(AuraTheme.sizing.iconXl)
                                            )
                                        }
                                    }
                                }
                            }
                            2 -> {
                                // Full Expanded Tabs
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(AuraTheme.spacing.xs),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    // Sliding Pill Indicator
                                    if (animatedIndicatorWidth > AuraTheme.spacing.none) {
                                        Box(
                                            modifier = Modifier
                                                .offset(x = animatedIndicatorOffset)
                                                .width(animatedIndicatorWidth)
                                                .fillMaxHeight()
                                                .background(
                                                    color = colors.activeIndicatorColor,
                                                    shape = shape
                                                )
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        items.forEachIndexed { index, item ->
                                            val isSelected = selectedIndex == index

                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight()
                                                    .onGloballyPositioned { coordinates ->
                                                        val pos = coordinates.positionInParent()
                                                        with(density) {
                                                            itemOffsets[index] = pos.x.toDp()
                                                            itemWidths[index] = coordinates.size.width.toDp()
                                                        }
                                                    }
                                                    .clickable(
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = if (enableItemRipple) ripple(color = colors.selectedItemColor.copy(alpha = 0.15f)) else null
                                                    ) {
                                                        onItemSelected(index, item)
                                                    },
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                if (item.customIcon != null) {
                                                    item.customIcon(isSelected)
                                                } else {
                                                    val icon = if (isSelected) (item.selectedIcon ?: item.icon) else item.icon
                                                    if (icon != null) {
                                                        Icon(
                                                            imageVector = icon,
                                                            contentDescription = item.title,
                                                            tint = if (isSelected) colors.selectedItemColor else colors.unselectedItemColor,
                                                            modifier = Modifier.size(AuraTheme.sizing.iconXl)
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(AuraTheme.spacing.xxs))

                                                Text(
                                                    text = item.title,
                                                    style = AuraTheme.typography.caption,
                                                    color = if (isSelected) colors.selectedItemColor else colors.unselectedItemColor,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // RIGHT SEGMENT (Action / Search Box)
                Surface(
                    modifier = Modifier
                        .height(currentHeight)
                        .then(
                            if (isExtra) Modifier.weight(rightWeight) else Modifier.width(currentHeight)
                        )
                        .clip(shape)
                        .border(
                            BorderStroke(AuraTheme.sizing.strokeThin, colors.borderColor),
                            shape
                        )
                        .then(surfaceModifier),
                    shape = shape,
                    color = colors.containerColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = if (enableItemRipple) ripple(color = colors.actionContainerColor.copy(alpha = 0.2f)) else null
                            ) {
                                if (!isExtra) onActionClick()
                            }
                            .padding(horizontal = if (isExtra) AuraTheme.spacing.base else AuraTheme.spacing.none),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (isExtra) Arrangement.Start else Arrangement.Center
                    ) {
                        // Action Icon (Animated swap between Search / Action)
                        AnimatedContent(
                            targetState = isExtra,
                            transitionSpec = {
                                (fadeIn(tween(180)) + scaleIn(initialScale = 0.85f))
                                    .togetherWith(fadeOut(tween(140)) + scaleOut(targetScale = 0.85f))
                            },
                            label = "AuraBottomBarActionIcon"
                        ) { extraActive ->
                            if (extraActive) {
                                Icon(
                                    imageVector = AuraIcons.Search,
                                    contentDescription = "Search",
                                    tint = colors.unselectedItemColor,
                                    modifier = Modifier.size(AuraTheme.sizing.iconLg)
                                )
                            } else {
                                if (actionIcon != null) {
                                    actionIcon(false)
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(AuraTheme.sizing.iconXxl)
                                            .background(colors.actionContainerColor, AuraTheme.shapes.pill),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = AuraIcons.Swap,
                                            contentDescription = "Action",
                                            tint = colors.actionContentColor,
                                            modifier = Modifier.size(AuraTheme.sizing.iconMd)
                                        )
                                    }
                                }
                            }
                        }

                        // Search Input Bar (Visible only when isExtra = true)
                        AnimatedVisibility(
                            visible = isExtra,
                            enter = fadeIn(tween(200, delayMillis = 60)) +
                                    expandHorizontally(
                                        expandFrom = Alignment.Start,
                                        animationSpec = AuraTheme.motion.intSizeSpringSnappy
                                    ),
                            exit = fadeOut(tween(120)) +
                                    shrinkHorizontally(
                                        shrinkTowards = Alignment.Start,
                                        animationSpec = AuraTheme.motion.intSizeSpringSnappy
                                    )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(AuraTheme.spacing.sm))

                                Box(modifier = Modifier.weight(1f)) {
                                    if (searchValue.isEmpty()) {
                                        Text(
                                            text = searchPlaceholder,
                                            style = AuraTheme.typography.bodyMedium,
                                            color = colors.searchPlaceholderColor
                                        )
                                    }

                                    BasicTextField(
                                        value = searchValue,
                                        onValueChange = onSearchValueChange,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .onFocusChanged { onTyping(it.isFocused) },
                                        textStyle = AuraTheme.typography.bodyMedium.copy(
                                            color = colors.searchTextColor
                                        ),
                                        singleLine = true,
                                        cursorBrush = SolidColor(colors.selectedItemColor),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()
                                                keyboard?.hide()
                                                onSearchDone?.invoke()
                                            }
                                        )
                                    )
                                }

                                if (searchValue.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .size(AuraTheme.sizing.iconLg)
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = ripple(bounded = false, radius = AuraTheme.spacing.md)
                                            ) {
                                                onSearchValueChange("")
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = AuraIcons.Close,
                                            contentDescription = "Clear",
                                            tint = colors.unselectedItemColor,
                                            modifier = Modifier.size(AuraTheme.sizing.iconSm)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
