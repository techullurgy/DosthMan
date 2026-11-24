package com.techullurgy.dosthman.shared.screens.workspace.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import com.techullurgy.dosthman.domain.models.SavedHttpRequest
import com.techullurgy.dosthman.shared.HttpRequestDraftStore
import com.techullurgy.dosthman.shared.screens.workspace.core.LocalRequestsTabsSynchronizer
import com.techullurgy.dosthman.shared.uimodels.UiHttpMethod
import com.techullurgy.dosthman.shared.uimodels.toUiHttpRequest
import org.koin.compose.koinInject
import kotlin.time.Clock

@Composable
internal fun WorkspaceSidebar(
    modifier: Modifier = Modifier,
    onCloseTab: ((String) -> Unit)? = null,
    onSelectTabOverride: ((String) -> Unit)? = null,
) {
    val requestsTabsSynchronizer = LocalRequestsTabsSynchronizer.current

    val openedTabs = requestsTabsSynchronizer.openedTabs
    val selectedTab = requestsTabsSynchronizer.selectedTab

    val handleSelectTab: (String) -> Unit = { key ->
        if (onSelectTabOverride != null) {
            onSelectTabOverride(key)
        } else {
            requestsTabsSynchronizer.openTab(key)
        }
    }

    val onNewRequest = {
        val newKey = "${Clock.System.now().toEpochMilliseconds()}"
        handleSelectTab(newKey)
    }

    val storage = koinInject<HttpRequestPersistentStorage>()
    val draftStore = koinInject<HttpRequestDraftStore>()
    val savedRequests by storage.allSavedRequests().collectAsState(initial = emptyList())

    val savedMap = remember(savedRequests) { savedRequests.associateBy { it.requestKey } }

    val currentOpenTabs = openedTabs.toList()
    val unOpenedSavedRequests = savedRequests.filter { it.requestKey !in currentOpenTabs }

    var searchQuery by remember { mutableStateOf("") }

    val matchesFilter: (String, SavedHttpRequest?) -> Boolean = { key, saved ->
        if (searchQuery.isBlank()) {
            true
        } else {
            val draft = draftStore.drafts[key]
            val method = draft?.method ?: saved?.request?.toUiHttpRequest()?.method ?: UiHttpMethod.Get
            val url = draft?.url?.text?.toString() ?: saved?.request?.url ?: ""
            val name = saved?.name
            val displayName = name?.takeIf { it.isNotBlank() } ?: url.ifBlank { "Untitled Request" }

            displayName.contains(searchQuery, ignoreCase = true) ||
                url.contains(searchQuery, ignoreCase = true) ||
                method.name.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredOpenedTabs = currentOpenTabs.filter { matchesFilter(it, savedMap[it]) }
    val filteredUnOpenedSavedRequests = unOpenedSavedRequests.filter { matchesFilter(it.requestKey, it) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Sidebar Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Requests",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            IconButton(
                onClick = onNewRequest,
                modifier = Modifier.size(32.dp),
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        // Search Filter
        if (currentOpenTabs.isNotEmpty() || unOpenedSavedRequests.isNotEmpty()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search requests...",
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            )
        }

        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))

        if (filteredOpenedTabs.isEmpty() && filteredUnOpenedSavedRequests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (currentOpenTabs.isEmpty() && unOpenedSavedRequests.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "No requests",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Click + to create a request",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                } else {
                    Text(
                        text = "No matches found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp),
            ) {
                // Section 1: Opened Tabs
                if (filteredOpenedTabs.isNotEmpty()) {
                    item(key = "header_opened_tabs") {
                        Text(
                            text = "Opened Tabs (${filteredOpenedTabs.size})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }

                    items(filteredOpenedTabs, key = { "opened_$it" }) { key ->
                        SidebarRequestItem(
                            requestKey = key,
                            isSelected = key == selectedTab,
                            isSaved = key in savedMap,
                            isOpened = true,
                            savedRequest = savedMap[key],
                            onClick = { handleSelectTab(key) },
                            onClose = onCloseTab?.let { close -> { close(key) } },
                        )
                    }
                }

                // Section 2: UnOpened Saved Tabs
                if (filteredUnOpenedSavedRequests.isNotEmpty()) {
                    item(key = "header_saved_tabs") {
                        Text(
                            text = "Saved Requests (${filteredUnOpenedSavedRequests.size})",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }

                    items(filteredUnOpenedSavedRequests, key = { "saved_${it.requestKey}" }) { saved ->
                        SidebarRequestItem(
                            requestKey = saved.requestKey,
                            isSelected = saved.requestKey == selectedTab,
                            isSaved = true,
                            isOpened = false,
                            savedRequest = saved,
                            onClick = { handleSelectTab(saved.requestKey) },
                            onClose = null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarRequestItem(
    requestKey: String,
    isSelected: Boolean,
    isSaved: Boolean,
    isOpened: Boolean,
    savedRequest: SavedHttpRequest?,
    onClick: () -> Unit,
    onClose: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val storage = koinInject<HttpRequestPersistentStorage>()
    val draftStore = koinInject<HttpRequestDraftStore>()

    val savedName by storage.savedRequestName(requestKey).collectAsState(initial = savedRequest?.name)
    val draft = draftStore.drafts[requestKey]

    val savedUi = savedRequest?.request?.toUiHttpRequest()
    val method = draft?.method ?: savedUi?.method ?: UiHttpMethod.Get
    val url = draft?.url?.text?.toString() ?: savedRequest?.request?.url ?: ""
    val name = savedName?.takeIf { it.isNotBlank() } ?: savedRequest?.name
    val displayName = name?.takeIf { it.isNotBlank() } ?: url.ifBlank { "Untitled Request" }

    // Check if the tab has unsaved changes:
    // 1. Never saved (not in persistent storage)
    // 2. Saved, but draft exists and differs from persistent storage
    val isUnsaved = remember(isSaved, draft, savedUi) {
        if (!isSaved) {
            true
        } else if (draft != null && savedUi != null) {
            !draft.matches(savedUi)
        } else {
            false
        }
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Method Badge
        Text(
            text = method.name.uppercase(),
            color = method.color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(44.dp),
        )

        // Request Name & URL
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )

                // Unsaved indicator dot
                if (isUnsaved) {
                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(7.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }

                if (!isSaved) {
                    Text(
                        text = "Draft",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
            }
            if (name?.isNotBlank() == true && url.isNotBlank()) {
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        // Close button for opened tabs
        if (isOpened && onClose != null) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(24.dp),
            ) {
                Text(
                    text = "✕",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
