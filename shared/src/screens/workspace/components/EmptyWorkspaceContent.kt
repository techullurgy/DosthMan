package com.techullurgy.dosthman.shared.screens.workspace.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import org.koin.compose.koinInject


@Composable
internal fun EmptyWorkspaceContent(
    onNewRequest: () -> Unit,
    onOpenRequest: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val storage = koinInject<HttpRequestPersistentStorage>()
    val savedRequests by storage.allSavedRequests().collectAsState(initial = emptyList())

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "No request selected",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onNewRequest) {
                Text("New Request")
            }

            if (savedRequests.isNotEmpty()) {
                Text(
                    text = "Saved Requests",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(savedRequests) { saved ->
                        val label = saved.name?.takeIf { it.isNotBlank() }
                            ?: "${saved.request.method.name} ${saved.request.url.ifBlank { "Untitled" }}"
                        SuggestionChip(
                            onClick = { onOpenRequest(saved.requestKey) },
                            label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                        )
                    }
                }
            }
        }
    }
}
