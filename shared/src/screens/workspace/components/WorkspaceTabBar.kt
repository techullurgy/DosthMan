package com.techullurgy.dosthman.shared.screens.workspace.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import com.techullurgy.dosthman.shared.components.EditableText
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
internal fun WorkspaceTabBar(
    openedTabs: List<String>,
    selectedTab: String?,
    onSelectTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
    onAddTab: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (openedTabs.isNotEmpty()) {
            val selectedIndex = openedTabs.indexOf(selectedTab).coerceAtLeast(0)
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 8.dp,
                divider = {},
                modifier = Modifier.weight(1f),
            ) {
                openedTabs.forEach { tabKey ->
                    WorkspaceTabItem(
                        tabKey = tabKey,
                        isSelected = tabKey == selectedTab,
                        onSelectTab = onSelectTab,
                        onCloseTab = onCloseTab,
                    )
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(
                    text = "No open tabs",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        IconButton(
            onClick = onAddTab,
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
}


@Composable
private fun WorkspaceTabItem(
    tabKey: String,
    isSelected: Boolean,
    onSelectTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
) {
    val storage = koinInject<HttpRequestPersistentStorage>()
    val coroutineScope = rememberCoroutineScope()
    val savedName by storage.savedRequestName(tabKey).collectAsState(initial = null)
    val tabTitle = savedName?.takeIf { it.isNotBlank() } ?: "Tab $tabKey"

    Tab(
        selected = isSelected,
        onClick = { onSelectTab(tabKey) },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                EditableText(
                    text = tabTitle,
                    onTextChange = { newName ->
                        coroutineScope.launch {
                            storage.saveName(tabKey, newName)
                        }
                    },
                    singleLine = true,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(
                    onClick = { onCloseTab(tabKey) },
                    modifier = Modifier.size(18.dp),
                ) {
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        },
    )
}
