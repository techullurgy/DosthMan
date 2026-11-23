package com.techullurgy.dosthman.shared.screens.workspace.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.shared.modifiers.preserveFocusOnTapBounds
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestSectionViewModel
import com.techullurgy.dosthman.shared.uimodels.EditableKeyValueItem
import com.techullurgy.dosthman.shared.uimodels.EditableHttpRequest

@Composable
fun RequestSection(
    viewModel: HttpRequestSectionViewModel,
    modifier: Modifier = Modifier,
) {
    val editableRequest = viewModel.editableHttpRequest

    if(editableRequest == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    RequestSectionContent(
        request = editableRequest,
        modifier = modifier,
    )
}

@Composable
private fun RequestSectionContent(
    request: EditableHttpRequest,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(RequestTab.Params) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
            RequestTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        val count = when (tab) {
                            RequestTab.Params -> request.params.size
                            RequestTab.Headers -> request.headers.size
                            RequestTab.Body -> 0
                        }
                        Text(if (count > 0) "${tab.label} ($count)" else tab.label)
                    },
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedTab) {
                RequestTab.Params -> KeyValueEditor(
                    items = request.params,
                    onAdd = request::addParam,
                    onRemove = request::removeParam,
                    addLabel = "Add Param",
                )

                RequestTab.Headers -> KeyValueEditor(
                    items = request.headers,
                    onAdd = request::addHeader,
                    onRemove = request::removeHeader,
                    addLabel = "Add Header",
                )

                RequestTab.Body -> RawBodyEditor(
                    bodyState = request.textBody,
                )
            }
        }
    }
}

@Composable
private fun KeyValueEditor(
    items: List<EditableKeyValueItem>,
    onAdd: () -> Unit,
    onRemove: (Long) -> Unit,
    addLabel: String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = items, key = { it.id }) { item ->
            KeyValueRow(
                item = item,
                onRemove = { onRemove(item.id) },
            )
        }

        item(key = "add_button") {
            TextButton(onClick = onAdd) {
                //Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text(addLabel)
            }
        }
    }
}

@Composable
private fun KeyValueRow(
    item: EditableKeyValueItem,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.enabled,
            onCheckedChange = { item.enabled = it },
        )

        OutlinedTextField(
            state = item.key,
            placeholder = { Text("Key") },
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .weight(1f)
                .preserveFocusOnTapBounds(),
        )

        OutlinedTextField(
            state = item.value,
            placeholder = { Text("Value") },
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .weight(1f)
                .preserveFocusOnTapBounds(),
        )

        TextButton(onClick = onRemove) {
            // Icon(Icons.Default.Delete, contentDescription = "Remove")
            Text("Remove")
        }
    }
}

@Composable
private fun RawBodyEditor(
    bodyState: TextFieldState,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        state = bodyState,
        placeholder = { Text("{\n  \"key\": \"value\"\n}") },
        textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .preserveFocusOnTapBounds(),
    )
}

private enum class RequestTab(val label: String) {
    Params("Params"),
    Headers("Headers"),
    Body("Body"),
}