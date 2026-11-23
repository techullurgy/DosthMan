package com.techullurgy.dosthman.shared.screens.workspace.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.shared.modifiers.preserveFocusOnTapBounds
import com.techullurgy.dosthman.shared.uimodels.EditableHttpRequest
import com.techullurgy.dosthman.shared.uimodels.UiHttpMethod


@Composable
internal fun WorkspaceUrlBar(
    editableHttpRequest: EditableHttpRequest,
    isSending: Boolean,
    isSaveable: Boolean,
    onSend: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var methodExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Method Dropdown
        Box {
            TextButton(onClick = { methodExpanded = true }) {
                Text(
                    text = editableHttpRequest.method.name.uppercase(),
                    color = editableHttpRequest.method.color,
                    fontWeight = FontWeight.Bold,
                )
            }
            DropdownMenu(
                expanded = methodExpanded,
                onDismissRequest = { methodExpanded = false },
            ) {
                UiHttpMethod.entries.forEach { method ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = method.name.uppercase(),
                                color = method.color,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        onClick = {
                            editableHttpRequest.method = method
                            methodExpanded = false
                        },
                    )
                }
            }
        }

        // URL field
        OutlinedTextField(
            state = editableHttpRequest.url,
            placeholder = { Text("https://example.com/api") },
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .weight(1f)
                .preserveFocusOnTapBounds(),
        )

        // Save Button
        TextButton(
            onClick = onSave,
            enabled = isSaveable,
        ) {
            Text("Save")
        }

        // Send Button
        Button(
            onClick = onSend,
            enabled = !isSending,
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Text("Send")
            }
        }
    }
}