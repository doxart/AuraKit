package com.doxart.aurakit.components.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.foundation.AuraIcons
import com.doxart.aurakit.generated.resources.Res
import com.doxart.aurakit.generated.resources.compose_multiplatform
import com.doxart.aurakit.theme.AuraTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Premium TopAppBar for AuraKit, faithfully replicating the easywallet ViewController pattern.
 *
 * Uses a gradient overlay background (from theme background to transparent) with
 * [CenterAlignedTopAppBar] (when [centered] is true) or left-aligned [TopAppBar].
 * Action buttons are provided via the [actions] slot and are expected to use
 * [AuraAppBarActionGroup] + [AuraAppBarAction] with [AnimatedVisibility] for
 * page-based conditional action rendering.
 *
 * @param modifier Root modifier for the app bar container.
 * @param title Custom composable title content. If null, [titleText] will be used.
 * @param titleText Plain string title formatted with [AuraTheme.typography.titleLarge].
 * @param style Visual style determining background translucency, gradients, and borders.
 * @param navigationIcon Custom composable for leading icon. If null and [onNavigationClick] is provided,
 *                       a standard [AuraIcons.Menu] icon button is rendered.
 * @param onNavigationClick Callback triggered when the default navigation icon is clicked.
 * @param actions Slot for trailing actions — typically an [AuraAppBarActionGroup] with
 *               page-conditional [AuraAppBarAction] items wrapped in [AnimatedVisibility].
 * @param colors Styling palette resolved via [AuraTopAppBarDefaults.colors].
 * @param showScrim When true, draws the multi-stop vertical gradient scrim for transparent/gradient styles.
 * @param scrollBehavior Scroll behavior contract for collapsing/fading headers.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraTopAppBar(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    titleText: String? = null,
    style: AuraTopAppBarStyle = AuraTopAppBarStyle.Transparent,
    navigationIcon: (@Composable () -> Unit)? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    colors: AuraTopAppBarColors = AuraTopAppBarDefaults.colors(style = style),
    showScrim: Boolean = true,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val m3Colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
        navigationIconContentColor = colors.navigationIconContentColor,
        titleContentColor = colors.titleContentColor,
        actionIconContentColor = colors.actionIconContentColor
    )

    val resolvedTitle: @Composable () -> Unit = {
        if (title != null) {
            title()
        } else if (!titleText.isNullOrBlank()) {
            Text(
                text = titleText,
                style = AuraTheme.typography.titleLarge,
                color = colors.titleContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    val resolvedNavIcon: @Composable () -> Unit = {
        if (navigationIcon != null) {
            navigationIcon()
        } else if (onNavigationClick != null) {
            IconButton(
                onClick = onNavigationClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colors.navigationIconContentColor
                )
            ) {
                Icon(
                    imageVector = AuraIcons.Menu,
                    contentDescription = "Navigation Menu",
                    modifier = Modifier.size(AuraTheme.sizing.iconLg)
                )
            }
        }
    }

    Box(modifier = modifier) {
        // Gradient overlay background (BlackToNone equivalent) or solid container
        Box(
            Modifier
                .fillMaxWidth()
                .matchParentSize()
                .then(
                    if (showScrim && colors.gradientBrush != null) {
                        Modifier.background(colors.gradientBrush)
                    } else if (colors.containerColor != Color.Transparent) {
                        Modifier.background(colors.containerColor)
                    } else {
                        Modifier
                    }
                )
        )

        if (navigationIcon != null) {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxWidth().padding(horizontal = AuraTopAppBarDefaults.HorizontalPadding),
                colors = m3Colors,
                title = resolvedTitle,
                navigationIcon = resolvedNavIcon,
                actions = {
                    if (actions != null) actions()
                }
            )
        } else {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxWidth().padding(end = AuraTopAppBarDefaults.HorizontalPadding),
                colors = m3Colors,
                title = resolvedTitle,
                navigationIcon = resolvedNavIcon,
                actions = {
                    if (actions != null) actions()
                }
            )
        }
    }
}

/**
 * Glassmorphism-styled container for grouping [AuraAppBarAction] items in [AuraTopAppBar].
 *
 * Mirrors the easywallet pattern where actions sit inside a `Surface` with rounded corners,
 * a translucent background, and horizontal spacing. Each child should be an [AuraAppBarAction]
 * optionally wrapped in [AnimatedVisibility] for page-conditional display.
 *
 * @param modifier Modifier for the container.
 * @param containerColor Background color of the action group container.
 * @param content The action items — typically [AuraAppBarAction] instances.
 */
@Composable
fun AuraAppBarActionGroup(
    modifier: Modifier = Modifier,
    containerColor: Color = AuraTheme.colors.surfaceVariant.copy(alpha = 0.65f),
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .background(
                color = containerColor,
                shape = AuraTheme.shapes.pill
            )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(AuraTheme.spacing.xs),
            horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * Individual action icon button for [AuraTopAppBar], matching the easywallet pattern.
 *
 * Each action is a fixed-size [IconButton] with transparent background.
 * Designed to be used inside [AuraAppBarActionGroup] and optionally wrapped in
 * [AnimatedVisibility] for page-conditional display.
 *
 * @param icon The [ImageVector] to display.
 * @param onClick Callback when the action is pressed.
 * @param modifier Modifier for the icon button.
 * @param contentDescription Accessibility label for the icon.
 * @param visible Controls visibility with animated enter/exit transitions.
 *               When null, the action is always visible (no AnimatedVisibility wrapper).
 * @param contentColor Tint color for the icon.
 */
@Composable
fun AuraAppBarAction(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    backgroundImage: DrawableResource? = null,
    visible: Boolean? = null,
    contentColor: Color = AuraTheme.colors.textPrimary,
    content: (@Composable () -> Unit)? = null
) {
    val actionContent: @Composable () -> Unit = {
        IconButton(
            modifier = modifier.size(AuraTheme.sizing.controlSm),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = contentColor
            ),
            onClick = onClick,
        ) {
            if (content != null) {
                content()
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AuraTheme.spacing.xs),
                    tint = contentColor
                )
            }
        }
    }

    if (visible != null) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(200)) +
                    expandHorizontally(
                        expandFrom = Alignment.End,
                        animationSpec = tween(250)
                    ),
            exit = fadeOut(animationSpec = tween(150)) +
                    shrinkHorizontally(
                        shrinkTowards = Alignment.End,
                        animationSpec = tween(200)
                    )
        ) {
            actionContent()
        }
    } else {
        actionContent()
    }
}
