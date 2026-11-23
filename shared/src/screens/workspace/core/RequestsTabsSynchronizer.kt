package com.techullurgy.dosthman.shared.screens.workspace.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

internal class RequestsTabsSynchronizer(
    private val openedTabKeys: SnapshotStateList<String>,
    private val selectedTabKeyState: MutableState<String?>,
) {
    val openedTabs get() = openedTabKeys
    val selectedTab get() = selectedTabKeyState.value

    fun openTab(requestKey: String) {
        if(requestKey !in openedTabKeys) {
            openedTabKeys.add(requestKey)
        }
        selectedTabKeyState.value = requestKey
    }

    fun removeTab(requestKey: String) {
        val index = openedTabKeys.indexOf(requestKey)
        if(index == -1) return

        openedTabKeys.removeAt(index)
        if (selectedTabKeyState.value == requestKey) {
            // Fall back to the previous tab, or the new first tab, or null if empty
            selectedTabKeyState.value = openedTabKeys.getOrNull((index - 1).coerceAtLeast(0))
        }
    }
}
