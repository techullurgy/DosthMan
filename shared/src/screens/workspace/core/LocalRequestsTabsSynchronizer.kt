package com.techullurgy.dosthman.shared.screens.workspace.core

import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalRequestsTabsSynchronizer = staticCompositionLocalOf<RequestsTabsSynchronizer> {
    error("LocalRequestsTabsSynchronizer was not provided")
}