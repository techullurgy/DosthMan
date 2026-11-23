package com.techullurgy.dosthman.shared.screens.workspace.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreProvider
import androidx.window.core.layout.WindowSizeClass

@Composable
internal fun ResponsiveApiWorkspaceSection(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        val windowInfo = currentWindowAdaptiveInfoV2()

        val isDesktopMode = windowInfo.windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
        )

        val requestTabsViewModelStoreProvider = rememberViewModelStoreProvider()
        val requestTabsSaveableStateHolder = rememberSaveableStateHolder()

        if(isDesktopMode) {
            DesktopApiWorkspaceSection(
                requestTabsViewModelStoreProvider = requestTabsViewModelStoreProvider,
                requestTabsSaveableStateHolder = requestTabsSaveableStateHolder,
            )
        } else {
            MobileApiWorkspaceSection(
                requestTabsViewModelStoreProvider = requestTabsViewModelStoreProvider,
                requestTabsSaveableStateHolder = requestTabsSaveableStateHolder,
            )
        }
    }
}