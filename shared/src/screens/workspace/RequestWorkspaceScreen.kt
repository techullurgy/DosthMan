package com.techullurgy.dosthman.shared.screens.workspace

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import com.techullurgy.dosthman.shared.screens.workspace.core.LocalRequestsTabsSynchronizer
import com.techullurgy.dosthman.shared.screens.workspace.core.RequestsTabsSynchronizer
import com.techullurgy.dosthman.shared.screens.workspace.sections.ResponsiveApiWorkspaceSection

@Composable
fun ApiWorkspaceScreen(
    modifier: Modifier = Modifier
) {
    val openedTabKeys = rememberSerializable(
        serializer = SnapshotStateListSerializer<String>()
    ) { mutableStateListOf() }

    val selectedKey = rememberSaveable { mutableStateOf<String?>(null) }

    val requestsTabsSynchronizer = remember(openedTabKeys, selectedKey) {
        RequestsTabsSynchronizer(openedTabKeys, selectedKey)
    }

    CompositionLocalProvider(
        LocalRequestsTabsSynchronizer provides requestsTabsSynchronizer
    ) {
        ResponsiveApiWorkspaceSection(
            modifier = modifier.fillMaxSize()
        )
    }
}