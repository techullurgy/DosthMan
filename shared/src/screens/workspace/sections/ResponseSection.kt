package com.techullurgy.dosthman.shared.screens.workspace.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.domain.models.ContentType
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpResponseSectionViewModel
import com.techullurgy.dosthman.shared.uimodels.UiHttpResponse
import com.techullurgy.dosthman.shared.uimodels.UiHttpStatusCode

private enum class ResponseTab(val label: String) {
    Body("Body"),
    ResponseHeaders("Headers"),
    RequestHeaders("Request Headers"),
}

@Composable
internal fun ResponseSection(
    viewModel: HttpResponseSectionViewModel,
    modifier: Modifier = Modifier,
) {
    when (val response = viewModel.response) {
        null -> EmptyResponseState(modifier = modifier)
        is UiHttpResponse.Failure -> FailureResponseState(
            message = response.message,
            modifier = modifier,
        )
        is UiHttpResponse.Data -> ResponseDataContent(
            response = response,
            modifier = modifier,
        )
    }
}

@Composable
private fun ResponseDataContent(
    response: UiHttpResponse.Data,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(ResponseTab.Body) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ResponseMetaBar(
            statusCode = response.statusCode,
            timeTaken = response.timeTaken,
            contentType = response.contentType,
        )

        PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
            ResponseTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        val count = when (tab) {
                            ResponseTab.Body -> 0
                            ResponseTab.ResponseHeaders -> response.responseHeaders.size
                            ResponseTab.RequestHeaders -> response.requestHeaders.size
                        }
                        Text(if (count > 0) "${tab.label} ($count)" else tab.label)
                    },
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                ResponseTab.Body -> ResponseBodyViewer(
                    content = response.content,
                    contentType = response.contentType,
                )

                ResponseTab.ResponseHeaders -> HeadersViewer(
                    headers = response.responseHeaders,
                )

                ResponseTab.RequestHeaders -> HeadersViewer(
                    headers = response.requestHeaders,
                )
            }
        }
    }
}

@Composable
private fun ResponseMetaBar(
    statusCode: UiHttpStatusCode,
    timeTaken: Long,
    contentType: ContentType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Adjust property access to match your UiHttpStatusCode definition
        Text(
            text = statusCode.toString(),
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = "${timeTaken} ms",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = contentType.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ResponseBodyViewer(
    content: String,
    contentType: ContentType,
    modifier: Modifier = Modifier,
) {
    if (content.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Empty response body",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    if (contentType == ContentType.ApplicationJson) {
//        JsonViewer(
//            json = content,
//            modifier = modifier
//                .fillMaxSize()
//                .padding(vertical = 8.dp),
//        )
    } else {
        SelectionContainer(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
        ) {
            Text(
                text = content,
                style = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
            )
        }
    }
}

@Composable
private fun HeadersViewer(
    headers: Map<String, List<String>>,
    modifier: Modifier = Modifier,
) {
    val entries = remember(headers) {
        headers.entries.flatMap { [key, values] ->
            values.map { value -> key to value }
        }
    }

    if (entries.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No headers",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    SelectionContainer(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(entries) { [key, value] ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(0.4f),
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                        ),
                        modifier = Modifier.weight(0.6f),
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
private fun EmptyResponseState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Send a request to see the response",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun FailureResponseState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}