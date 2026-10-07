package com.doxart.aurakit

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import com.doxart.aurakit.components.scaffold.AuraScaffold
import com.doxart.aurakit.components.scaffold.rememberAuraScaffoldState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.doxart.aurakit.components.badge.AuraBadge
import com.doxart.aurakit.components.badge.AuraBadgeSize
import com.doxart.aurakit.components.badge.AuraBadgeStyle
import com.doxart.aurakit.components.badge.AuraBadgeVariant
import com.doxart.aurakit.components.button.AuraButton
import com.doxart.aurakit.components.button.AuraButtonSize
import com.doxart.aurakit.components.button.AuraButtonVariant
import com.doxart.aurakit.components.card.AuraCard
import com.doxart.aurakit.components.card.AuraCardVariant
import com.doxart.aurakit.components.feedback.AuraSnackbarHost
import com.doxart.aurakit.components.feedback.AuraSnackbarResult
import com.doxart.aurakit.components.feedback.AuraSnackbarType
import com.doxart.aurakit.components.feedback.rememberAuraSnackbarHostState
import com.doxart.aurakit.components.navigation.AuraBottomBarItem
import com.doxart.aurakit.components.navigation.AuraBottomBarStyle
import com.doxart.aurakit.components.navigation.AuraExpandableBottomBar
import com.doxart.aurakit.components.list.AuraListItem
import com.doxart.aurakit.components.list.AuraListItemDefaults
import com.doxart.aurakit.components.reveal.AuraRevealAction
import com.doxart.aurakit.components.reveal.AuraRevealableItem
import com.doxart.aurakit.components.appbar.AuraAppBarAction
import com.doxart.aurakit.components.appbar.AuraAppBarActionGroup
import com.doxart.aurakit.components.appbar.AuraTopAppBar
import com.doxart.aurakit.components.appbar.AuraTopAppBarStyle
import com.doxart.aurakit.components.chart.AuraBarChart
import com.doxart.aurakit.components.chart.AuraBarData
import com.doxart.aurakit.components.chart.AuraLineChart
import com.doxart.aurakit.components.chart.AuraPointData
import com.doxart.aurakit.components.modal.AuraInputDialog
import com.doxart.aurakit.components.modal.AuraInputType
import com.doxart.aurakit.components.modal.AuraModalDialog
import com.doxart.aurakit.foundation.AuraIcons
import com.doxart.aurakit.theme.AuraDarkColors
import com.doxart.aurakit.theme.AuraLightColors
import com.doxart.aurakit.theme.AuraTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class DemoTransaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val isPositive: Boolean? = false,
    val icon: ImageVector
)

@Composable
@Preview
fun App() {
    var isDarkTheme by remember { mutableStateOf(true) }
    val colors = if (isDarkTheme) AuraDarkColors else AuraLightColors

    AuraTheme(
        darkTheme = isDarkTheme,
        colors = colors
    ) {
        val snackbarHostState = rememberAuraSnackbarHostState()
        val coroutineScope = rememberCoroutineScope()
        var isButtonLoading by remember { mutableStateOf(false) }

        // AuraScaffold & Bottom Bar States
        val scaffoldState = rememberAuraScaffoldState()
        var showScaffoldTopBar by remember { mutableStateOf(true) }
        var showScaffoldBottomBar by remember { mutableStateOf(true) }
        var showScaffoldFab by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf("") }

        // TopAppBar & BottomBar Showcase States
        var topAppBarStyle by remember { mutableStateOf(AuraTopAppBarStyle.Transparent) }
        var bottomBarStyle by remember { mutableStateOf(AuraBottomBarStyle.Elevated) }
        var showBarScrims by remember { mutableStateOf(true) }

        // Charts Showcase Data & States
        val monthlyBarData = remember {
            listOf(
                AuraBarData("1", "Apr", 75f, "$750"),
                AuraBarData("2", "May", 40f, "$400"),
                AuraBarData("3", "Jun", 22f, "$220"),
                AuraBarData("4", "Jul", 50f, "$500"),
                AuraBarData("5", "Aug", 90f, "$900"),
                AuraBarData("6", "Sep", 65f, "$650")
            )
        }
        var selectedBarIndex by remember { mutableStateOf(5) }

        val monthlyPointData = remember {
            listOf(
                AuraPointData("Apr", 320f, "$320.00"),
                AuraPointData("May", 480f, "$480.00"),
                AuraPointData("Jun", 290f, "$290.00"),
                AuraPointData("Jul", 610f, "$610.00"),
                AuraPointData("Aug", 550f, "$550.00"),
                AuraPointData("Sep", 780f, "$780.00")
            )
        }
        var scrubbedPoint by remember { mutableStateOf<AuraPointData?>(null) }

        // Modal & Input States
        var showCurrencyInput by remember { mutableStateOf(false) }
        var showCodeInput by remember { mutableStateOf(false) }
        var showPhoneInput by remember { mutableStateOf(false) }
        var showGenericModal by remember { mutableStateOf(false) }

        var confirmedCurrency by remember { mutableStateOf("$2,220.00") }
        var confirmedCode by remember { mutableStateOf("482910") }
        var confirmedPhone by remember { mutableStateOf("+1 (555) 019-2831") }

        val bottomNavItems = remember {
            listOf(
                AuraBottomBarItem(data = "home", title = "Home", icon = AuraIcons.Home),
                AuraBottomBarItem(data = "wallet", title = "Wallet", icon = AuraIcons.Wallet),
                AuraBottomBarItem(data = "swap", title = "Swap", icon = AuraIcons.Swap),
                AuraBottomBarItem(data = "search", title = "Search", icon = AuraIcons.Search)
            )
        }

        val transactionList = remember {
            mutableStateListOf(
                DemoTransaction("1", "Netflix Subscription", "Today • 10:00 am", "-$16.99", false, AuraIcons.Wallet),
                DemoTransaction("2", "Upwork Freelance", "Yesterday • 04:30 pm", "+$850.00", true, AuraIcons.Swap),
                DemoTransaction("3", "Apple Services", "Sep 28 • 01:15 pm", "-$9.99", false, AuraIcons.Home)
            )
        }

        var isLastVisible by remember { mutableStateOf(false) }

        AuraScaffold(
            state = scaffoldState,
            showTopBar = showScaffoldTopBar,
            showBottomBar = showScaffoldBottomBar,
            titleText = "AuraKit Showcase",
            topBarStyle = topAppBarStyle,
            showTopBarScrim = showBarScrims,
            bottomBarStyle = bottomBarStyle,
            showBottomBarScrim = showBarScrims,
            topBarActions = {
                AuraAppBarActionGroup {
                    AuraAppBarAction(
                        icon = AuraIcons.Search,
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Search tapped",
                                    type = AuraSnackbarType.Info
                                )
                            }
                        },
                        visible = isLastVisible
                    )
                    AuraAppBarAction(
                        icon = AuraIcons.Swap,
                        onClick = {
                            isLastVisible = true
                        }
                    )
                    AuraAppBarAction(
                        icon = AuraIcons.Menu,
                        onClick = {
                            isLastVisible = false
                        }
                    )
                }
            },
            bottomBarItems = bottomNavItems,
            selectedBottomBarIndex = scaffoldState.selectedBottomBarIndex,
            onBottomBarItemSelected = { index, item ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Tab: ${item.title}",
                        type = AuraSnackbarType.Info
                    )
                }
            },
            bottomBarSearchValue = searchQuery,
            onBottomBarSearchValueChange = { searchQuery = it },
            snackbarHost = {
                AuraSnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(AuraTheme.spacing.base)
                )
            },
            floatingActionButton = if (showScaffoldFab) {
                {
                    AuraButton(
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Floating Action Button tapped!",
                                    type = AuraSnackbarType.Success
                                )
                            }
                        },
                        variant = AuraButtonVariant.Primary,
                        size = AuraButtonSize.Medium
                    ) {
                        Icon(
                            imageVector = AuraIcons.Swap,
                            contentDescription = null,
                            modifier = Modifier.size(AuraTheme.sizing.iconMd)
                        )
                        Spacer(Modifier.width(AuraTheme.spacing.xs))
                        Text("Action")
                    }
                }
            } else null
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = AuraTheme.spacing.base,
                    top = paddingValues.calculateTopPadding(),
                    end = AuraTheme.spacing.base,
                    bottom = paddingValues.calculateBottomPadding() + AuraTheme.spacing.base
                ),
                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xl)
            ) {
                    // Header Bar
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xxs)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    Text(
                                        text = "AuraKit",
                                        style = AuraTheme.typography.headlineLarge,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Accent,
                                        style = AuraBadgeStyle.Subtle,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text("v0.0.1")
                                    }
                                }
                                Text(
                                    text = "Premium Jetpack Compose Multiplatform UI Kit",
                                    style = AuraTheme.typography.bodySmall,
                                    color = AuraTheme.colors.textSecondary
                                )
                            }

                            AuraButton(
                                onClick = { isDarkTheme = !isDarkTheme },
                                variant = AuraButtonVariant.Outline,
                                size = AuraButtonSize.Small
                            ) {
                                Text(if (isDarkTheme) "Dark" else "Light")
                            }
                        }
                    }

                    // AuraScaffold Architecture Showcase Card
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Elevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    Text(
                                        text = "AuraScaffold (AppScaffold)",
                                        style = AuraTheme.typography.titleMedium,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Accent,
                                        style = AuraBadgeStyle.Subtle,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text("Core Architecture")
                                    }
                                }

                                Text(
                                    text = "Faithful to easywallet's ViewController: Full-bleed content extends under floating bars. Status & navigation insets plus bar clearance are automatically calculated and passed as PaddingValues. Scroll to observe the bottom bar collapse/expand seamlessly.",
                                    style = AuraTheme.typography.bodySmall,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = { showScaffoldTopBar = !showScaffoldTopBar },
                                        variant = if (showScaffoldTopBar) AuraButtonVariant.Primary else AuraButtonVariant.Outline,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (showScaffoldTopBar) "TopBar: On" else "TopBar: Off")
                                    }

                                    AuraButton(
                                        onClick = { showScaffoldBottomBar = !showScaffoldBottomBar },
                                        variant = if (showScaffoldBottomBar) AuraButtonVariant.Primary else AuraButtonVariant.Outline,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (showScaffoldBottomBar) "BottomBar: On" else "BottomBar: Off")
                                    }

                                    AuraButton(
                                        onClick = { showScaffoldFab = !showScaffoldFab },
                                        variant = if (showScaffoldFab) AuraButtonVariant.Primary else AuraButtonVariant.Outline,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (showScaffoldFab) "FAB: On" else "FAB: Off")
                                    }
                                }

                                Text(
                                    text = "TopAppBar Style Preset:",
                                    style = AuraTheme.typography.labelSmall,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
                                ) {
                                    AuraTopAppBarStyle.entries.forEach { style ->
                                        AuraButton(
                                            onClick = { topAppBarStyle = style },
                                            variant = if (topAppBarStyle == style) AuraButtonVariant.Primary else AuraButtonVariant.Ghost,
                                            size = AuraButtonSize.Small
                                        ) {
                                            Text(style.name)
                                        }
                                    }
                                }

                                Text(
                                    text = "BottomBar Style Preset & Scrim:",
                                    style = AuraTheme.typography.labelSmall,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
                                ) {
                                    AuraBottomBarStyle.entries.forEach { style ->
                                        AuraButton(
                                            onClick = { bottomBarStyle = style },
                                            variant = if (bottomBarStyle == style) AuraButtonVariant.Primary else AuraButtonVariant.Ghost,
                                            size = AuraButtonSize.Small
                                        ) {
                                            Text(style.name)
                                        }
                                    }

                                    AuraButton(
                                        onClick = { showBarScrims = !showBarScrims },
                                        variant = if (showBarScrims) AuraButtonVariant.Secondary else AuraButtonVariant.Outline,
                                        size = AuraButtonSize.Small
                                    ) {
                                        Text(if (showBarScrims) "Scrims: On" else "Scrims: Off")
                                    }
                                }
                            }
                        }
                    }

                    // Expandable Bottom Bar Controller Showcase
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Elevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    Text(
                                        text = "AuraExpandableBottomBar",
                                        style = AuraTheme.typography.titleMedium,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Success,
                                        style = AuraBadgeStyle.Subtle,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text("Interactive Bar")
                                    }
                                }

                                Text(
                                    text = "Check out the interactive bottom navigation bar below! Use the controls to test spring animations, search expansion, and collapse modes.",
                                    style = AuraTheme.typography.bodySmall,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = { scaffoldState.toggleBottomBarExpand() },
                                        variant = if (scaffoldState.isBottomBarExpanded) AuraButtonVariant.Primary else AuraButtonVariant.Secondary,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (scaffoldState.isBottomBarExpanded) "Collapse Bar" else "Expand Bar")
                                    }

                                    AuraButton(
                                        onClick = { scaffoldState.toggleBottomBarExtra() },
                                        variant = if (scaffoldState.isBottomBarExtra) AuraButtonVariant.Primary else AuraButtonVariant.Secondary,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(if (scaffoldState.isBottomBarExtra) "Exit Search" else "Enter Search")
                                    }
                                }

                                if (searchQuery.isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
                                    ) {
                                        Text(
                                            text = "Current Query:",
                                            style = AuraTheme.typography.caption,
                                            color = AuraTheme.colors.textMuted
                                        )
                                        Text(
                                            text = searchQuery,
                                            style = AuraTheme.typography.caption,
                                            color = AuraTheme.colors.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Controllable Transactions Showcase Section
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Filled,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = AuraTheme.spacing.none
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(AuraTheme.spacing.base),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xxs)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                        ) {
                                            Text(
                                                text = "AuraRevealableItem",
                                                style = AuraTheme.typography.titleMedium,
                                                color = AuraTheme.colors.textPrimary
                                            )
                                            AuraBadge(
                                                variant = AuraBadgeVariant.Accent,
                                                style = AuraBadgeStyle.Subtle,
                                                size = AuraBadgeSize.Small
                                            ) {
                                                Text("Swipe-to-Reveal")
                                            }
                                        }
                                        Text(
                                            text = "Generic swipe-to-reveal container wrapping list items. Elastic drag & full swipe.",
                                            style = AuraTheme.typography.caption,
                                            color = AuraTheme.colors.textSecondary
                                        )
                                    }

                                    if (transactionList.isEmpty()) {
                                        AuraButton(
                                            onClick = {
                                                transactionList.addAll(
                                                    listOf(
                                                        DemoTransaction("1", "Netflix Subscription", "Today • 10:00 am", "-$16.99", false, AuraIcons.Wallet),
                                                        DemoTransaction("2", "Upwork Freelance", "Yesterday • 04:30 pm", "+$850.00", true, AuraIcons.Swap),
                                                        DemoTransaction("3", "Apple Services", "Sep 28 • 01:15 pm", "-$9.99", false, AuraIcons.Home)
                                                    )
                                                )
                                            },
                                            variant = AuraButtonVariant.Ghost,
                                            size = AuraButtonSize.Small
                                        ) {
                                            Text("Reset")
                                        }
                                    }
                                }

                                transactionList.forEachIndexed { index, tx ->
                                    key(tx.id) {
                                        AuraRevealableItem(
                                            actions = listOf(
                                                AuraRevealAction(
                                                    id = "edit_${tx.id}",
                                                    icon = AuraIcons.Edit,
                                                    contentDescription = "Edit",
                                                    containerColor = AuraTheme.colors.secondary,
                                                    contentColor = AuraTheme.colors.onSecondary,
                                                    onClick = {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                title = "Edit Action",
                                                                message = "Editing ${tx.title}",
                                                                type = AuraSnackbarType.Info
                                                            )
                                                        }
                                                    }
                                                ),
                                                AuraRevealAction(
                                                    id = "delete_${tx.id}",
                                                    icon = AuraIcons.Trash,
                                                    contentDescription = "Delete",
                                                    containerColor = AuraTheme.colors.error,
                                                    contentColor = AuraTheme.colors.onError,
                                                    isDestructive = true,
                                                    onClick = {
                                                        transactionList.removeAt(index)
                                                        coroutineScope.launch {
                                                            val result = snackbarHostState.showSnackbar(
                                                                title = "Item Deleted",
                                                                message = "${tx.title} removed",
                                                                type = AuraSnackbarType.Warning,
                                                                actionLabel = "Undo"
                                                            )
                                                            if (result == AuraSnackbarResult.ActionPerformed) {
                                                                transactionList.add(
                                                                    index.coerceAtMost(
                                                                        transactionList.size
                                                                    ), tx
                                                                )
                                                            }
                                                        }
                                                    }
                                                )
                                            ),
                                            onFullSwipe = {
                                                transactionList.removeAt(index)
                                                coroutineScope.launch {
                                                    val result = snackbarHostState.showSnackbar(
                                                        title = "Item Deleted (Full Swipe)",
                                                        message = "${tx.title} removed",
                                                        type = AuraSnackbarType.Warning,
                                                        actionLabel = "Undo"
                                                    )
                                                    if (result == AuraSnackbarResult.ActionPerformed) {
                                                        transactionList.add(
                                                            index.coerceAtMost(transactionList.size),
                                                            tx
                                                        )
                                                    }
                                                }
                                            },
                                            showDivider = index < transactionList.size - 1,
                                            dividerPaddingStart = AuraTheme.sizing.dividerIndent,
                                            actionShape = CircleShape
                                        ) {
                                            AuraListItem(
                                                headline = tx.title,
                                                supportingText = tx.subtitle,
                                                trailingText = tx.amount,
                                                leadingIcon = tx.icon,
                                                colors = AuraListItemDefaults.colors(
                                                    trailingContentColor = if (tx.isPositive == true) {
                                                        AuraTheme.colors.success
                                                    } else {
                                                        AuraTheme.colors.textPrimary
                                                    }
                                                ),
                                                onClick = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Selected: ${tx.title}",
                                                            type = AuraSnackbarType.Default
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Buttons Showcase Section
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Filled,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    Text(
                                        text = "AuraButton Variants",
                                        style = AuraTheme.typography.titleMedium,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Primary,
                                        style = AuraBadgeStyle.Subtle,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text("Spring Animated")
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Primary button clicked!",
                                                    type = AuraSnackbarType.Default
                                                )
                                            }
                                        },
                                        variant = AuraButtonVariant.Primary,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Primary")
                                    }

                                    AuraButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Secondary button clicked!",
                                                    type = AuraSnackbarType.Info
                                                )
                                            }
                                        },
                                        variant = AuraButtonVariant.Secondary,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Secondary")
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = {},
                                        variant = AuraButtonVariant.Outline,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Outline")
                                    }

                                    AuraButton(
                                        onClick = {},
                                        variant = AuraButtonVariant.Ghost,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Ghost")
                                    }

                                    AuraButton(
                                        onClick = {},
                                        variant = AuraButtonVariant.Destructive,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Delete")
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AuraButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                isButtonLoading = true
                                                delay(2000.milliseconds)
                                                isButtonLoading = false
                                            }
                                        },
                                        variant = AuraButtonVariant.Primary,
                                        isLoading = isButtonLoading,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Simulate Loading")
                                    }

                                    AuraButton(
                                        onClick = {},
                                        variant = AuraButtonVariant.Primary,
                                        enabled = false,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Disabled")
                                    }
                                }
                            }
                        }
                    }

                    // Badges & Status Section
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Outlined,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "AuraBadge Variants & Styles",
                                    style = AuraTheme.typography.titleMedium,
                                    color = AuraTheme.colors.textPrimary
                                )

                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraBadge(variant = AuraBadgeVariant.Primary, style = AuraBadgeStyle.Filled) { Text("Primary") }
                                    AuraBadge(variant = AuraBadgeVariant.Success, style = AuraBadgeStyle.Subtle) { Text("Success") }
                                    AuraBadge(variant = AuraBadgeVariant.Warning, style = AuraBadgeStyle.Subtle) { Text("Warning") }
                                    AuraBadge(variant = AuraBadgeVariant.Error, style = AuraBadgeStyle.Subtle) { Text("Error") }
                                    AuraBadge(variant = AuraBadgeVariant.Info, style = AuraBadgeStyle.Outlined) { Text("Info") }
                                    AuraBadge(variant = AuraBadgeVariant.Accent, style = AuraBadgeStyle.Subtle) { Text("Accent") }
                                }
                            }
                        }
                    }

                    // Interactive Snackbar Section
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Filled,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "AuraSnackbar Triggers",
                                    style = AuraTheme.typography.titleMedium,
                                    color = AuraTheme.colors.textPrimary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    title = "Success",
                                                    message = "Your settings have been saved successfully.",
                                                    type = AuraSnackbarType.Success,
                                                    actionLabel = "Undo"
                                                )
                                            }
                                        },
                                        variant = AuraButtonVariant.Secondary,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Success")
                                    }

                                    AuraButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    title = "Warning",
                                                    message = "Connection unstable. Retrying in 5 seconds.",
                                                    type = AuraSnackbarType.Warning
                                                )
                                            }
                                        },
                                        variant = AuraButtonVariant.Secondary,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Warning")
                                    }

                                    AuraButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    title = "Error",
                                                    message = "Failed to synchronize profile data.",
                                                    type = AuraSnackbarType.Error,
                                                    actionLabel = "Retry"
                                                )
                                            }
                                        },
                                        variant = AuraButtonVariant.Secondary,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Error")
                                    }
                                }
                            }
                        }
                    }

                    // Design System Tokens Preview
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Elevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    Text(
                                        text = "Design System Palette",
                                        style = AuraTheme.typography.titleMedium,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Accent,
                                        style = AuraBadgeStyle.Subtle,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text("Web Synced")
                                    }
                                }

                                Text(
                                    text = "Semantic Theme Tokens",
                                    style = AuraTheme.typography.labelMedium,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    ColorSwatch("Primary", AuraTheme.colors.primary, Modifier.weight(1f))
                                    ColorSwatch("Secondary", AuraTheme.colors.secondary, Modifier.weight(1f))
                                    ColorSwatch("Accent", AuraTheme.colors.accent, Modifier.weight(1f))
                                    ColorSwatch("Surface", AuraTheme.colors.surfaceElevated, Modifier.weight(1f))
                                }

                                Text(
                                    text = "Web Brand Palette",
                                    style = AuraTheme.typography.labelMedium,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
                                ) {
                                    ColorSwatch("Periwinkle", AuraTheme.brand.Accent, Modifier.weight(1f))
                                    ColorSwatch("Lime", AuraTheme.brand.Lime, Modifier.weight(1f))
                                    ColorSwatch("Violet", AuraTheme.brand.Violet, Modifier.weight(1f))
                                    ColorSwatch("Cyan", AuraTheme.brand.Cyan, Modifier.weight(1f))
                                    ColorSwatch("Orange", AuraTheme.brand.Orange, Modifier.weight(1f))
                                    ColorSwatch("Navy", AuraTheme.brand.Navy, Modifier.weight(1f))
                                }

                                Text(
                                    text = "Implementation Gradient",
                                    style = AuraTheme.typography.labelMedium,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(AuraTheme.spacing.lg)
                                        .clip(AuraTheme.shapes.sm)
                                        .background(AuraTheme.gradients.Implementation)
                                )
                            }
                        }
                    }

                    // --- 1. AuraTopAppBar Interactive Controls ---
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Elevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "TopAppBar Styles",
                                        style = AuraTheme.typography.titleMedium,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Accent,
                                        style = AuraBadgeStyle.Filled,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text(topAppBarStyle.name)
                                    }
                                }

                                Text(
                                    text = "Switch active TopAppBar background style to observe dynamic token changes above:",
                                    style = AuraTheme.typography.bodySmall,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
                                ) {
                                    AuraTopAppBarStyle.entries.forEach { style ->
                                        AuraButton(
                                            onClick = { topAppBarStyle = style },
                                            variant = if (topAppBarStyle == style) AuraButtonVariant.Primary else AuraButtonVariant.Outline,
                                            size = AuraButtonSize.Small
                                        ) {
                                            Text(style.name)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // --- 2. Aura Charts Showcase ---
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Elevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.lg),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Analytics & Charts",
                                            style = AuraTheme.typography.titleMedium,
                                            color = AuraTheme.colors.textPrimary
                                        )
                                        Text(
                                            text = "AuraLineChart (Scrubbing) & AuraBarChart (Hatch Pattern)",
                                            style = AuraTheme.typography.caption,
                                            color = AuraTheme.colors.textSecondary
                                        )
                                    }
                                    if (scrubbedPoint != null) {
                                        AuraBadge(
                                            variant = AuraBadgeVariant.Info,
                                            style = AuraBadgeStyle.Filled,
                                            size = AuraBadgeSize.Small
                                        ) {
                                            Text("${scrubbedPoint?.xLabel}: ${scrubbedPoint?.formattedValue}")
                                        }
                                    }
                                }

                                // Line Chart
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(AuraTheme.shapes.md)
                                        .background(AuraTheme.colors.surfaceVariant.copy(alpha = 0.25f))
                                        .padding(AuraTheme.spacing.xs)
                                ) {
                                    AuraLineChart(
                                        data = monthlyPointData,
                                        modifier = Modifier.fillMaxSize(),
                                        onPointSelected = { scrubbedPoint = it }
                                    )
                                }

                                // Bar Chart
                                Column(verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)) {
                                    Text(
                                        text = "Monthly Spending (Tap bars to inspect):",
                                        style = AuraTheme.typography.labelSmall,
                                        color = AuraTheme.colors.textSecondary
                                    )
                                    AuraBarChart(
                                        data = monthlyBarData,
                                        selectedIndex = selectedBarIndex,
                                        onBarClick = { index, _ -> selectedBarIndex = index },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    // --- 3. Aura Modals & Dynamic Input Dialogs Showcase ---
                    item {
                        AuraCard(
                            variant = AuraCardVariant.Elevated,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.md),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Modals & Dynamic Inputs",
                                        style = AuraTheme.typography.titleMedium,
                                        color = AuraTheme.colors.textPrimary
                                    )
                                    AuraBadge(
                                        variant = AuraBadgeVariant.Success,
                                        style = AuraBadgeStyle.Subtle,
                                        size = AuraBadgeSize.Small
                                    ) {
                                        Text("Physics Dismiss")
                                    }
                                }

                                Text(
                                    text = "Ready-to-use input dialogs with auto-masking, validation, and tactile in-dialog keypad:",
                                    style = AuraTheme.typography.bodySmall,
                                    color = AuraTheme.colors.textSecondary
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = { showCurrencyInput = true },
                                        variant = AuraButtonVariant.Secondary,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Currency ($)")
                                    }

                                    AuraButton(
                                        onClick = { showCodeInput = true },
                                        variant = AuraButtonVariant.Outline,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("OTP Code")
                                    }

                                    AuraButton(
                                        onClick = { showPhoneInput = true },
                                        variant = AuraButtonVariant.Outline,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Phone")
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(AuraTheme.spacing.sm)
                                ) {
                                    AuraButton(
                                        onClick = { showGenericModal = true },
                                        variant = AuraButtonVariant.Ghost,
                                        size = AuraButtonSize.Small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Generic Bottom Sheet")
                                    }
                                }

                                // Last Confirmed Results Display
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(AuraTheme.shapes.sm)
                                        .background(AuraTheme.colors.surfaceVariant.copy(alpha = 0.35f))
                                        .padding(AuraTheme.spacing.sm),
                                    verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xxs)
                                ) {
                                    Text(
                                        text = "Last Confirmed Inputs:",
                                        style = AuraTheme.typography.labelSmall,
                                        color = AuraTheme.colors.textSecondary
                                    )
                                    Text(
                                        text = "• Currency: $confirmedCurrency\n• OTP: $confirmedCode\n• Phone: $confirmedPhone",
                                        style = AuraTheme.typography.bodySmall.copy(color = AuraTheme.colors.textPrimary)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(AuraTheme.spacing.xxxl))
                    }
                }

                // Dialog Invocations
                if (showCurrencyInput) {
                    AuraInputDialog(
                        title = "Set Budget Amount",
                        subtitle = "Enter your monthly allowance limit",
                        inputType = AuraInputType.Currency("$"),
                        initialValue = "222000",
                        onDismissRequest = { showCurrencyInput = false },
                        onConfirm = { value ->
                            confirmedCurrency = AuraInputType.Currency("$").formatDisplay(value)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Budget confirmed: $confirmedCurrency",
                                    type = AuraSnackbarType.Success
                                )
                            }
                        }
                    )
                }

                if (showCodeInput) {
                    AuraInputDialog(
                        title = "Security Verification",
                        subtitle = "Enter 6-digit confirmation code",
                        inputType = AuraInputType.Code(length = 6),
                        initialValue = "482",
                        onDismissRequest = { showCodeInput = false },
                        onConfirm = { value ->
                            confirmedCode = value
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "OTP Verified: $confirmedCode",
                                    type = AuraSnackbarType.Success
                                )
                            }
                        }
                    )
                }

                if (showPhoneInput) {
                    AuraInputDialog(
                        title = "Contact Phone",
                        subtitle = "Enter recipient mobile number",
                        inputType = AuraInputType.Phone(),
                        initialValue = "5550192831",
                        onDismissRequest = { showPhoneInput = false },
                        onConfirm = { value ->
                            confirmedPhone = AuraInputType.Phone().formatDisplay(value)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Phone confirmed: $confirmedPhone",
                                    type = AuraSnackbarType.Info
                                )
                            }
                        }
                    )
                }

                if (showGenericModal) {
                    AuraModalDialog(
                        onDismissRequest = { showGenericModal = false }
                    ) { dismiss ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AuraTheme.spacing.base),
                            verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.base),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Custom Bottom Sheet",
                                style = AuraTheme.typography.titleLarge,
                                color = AuraTheme.colors.textPrimary
                            )
                            Text(
                                text = "AuraModalDialog accepts any generic composable slot. Swipe down with velocity or click outside to dismiss!",
                                style = AuraTheme.typography.bodyMedium,
                                color = AuraTheme.colors.textSecondary,
                                textAlign = TextAlign.Center
                            )
                            AuraButton(
                                onClick = dismiss,
                                variant = AuraButtonVariant.Outline,
                                size = AuraButtonSize.Medium,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Dismiss Sheet")
                            }
                        }
                    }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    label: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AuraTheme.spacing.xs)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AuraTheme.spacing.xxxl)
                .clip(AuraTheme.shapes.sm)
                .background(color)
        )
        Text(
            text = label,
            style = AuraTheme.typography.caption,
            color = AuraTheme.colors.textSecondary
        )
    }
}