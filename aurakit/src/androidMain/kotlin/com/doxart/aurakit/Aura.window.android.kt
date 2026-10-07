package com.doxart.aurakit

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
actual fun getFullScreenDialogProperties(): DialogProperties {
    return DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false,
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
}