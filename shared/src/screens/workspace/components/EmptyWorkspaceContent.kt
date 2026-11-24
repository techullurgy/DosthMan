package com.techullurgy.dosthman.shared.screens.workspace.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.EmptyWorkspaceViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun EmptyWorkspaceContent(
    onNewRequest: () -> Unit,
    onOpenRequest: (String) -> Unit = {},
    onToggleSidebar: (() -> Unit)? = null,
    showSavedRequests: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<EmptyWorkspaceViewModel>()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (onToggleSidebar != null) {
            IconButton(
                onClick = onToggleSidebar,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .size(36.dp),
            ) {
                Text(
                    text = "☰",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
        ) {
            Text(
                text = "No request selected",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onNewRequest) {
                    Text("New Request")
                }
                if (onToggleSidebar != null) {
                    OutlinedButton(onClick = onToggleSidebar) {
                        Text("View Requests")
                    }
                }
            }

            if (showSavedRequests && viewModel.savedRequests.isNotEmpty()) {
                Text(
                    text = "Saved Requests",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(viewModel.savedRequests) { saved ->
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
