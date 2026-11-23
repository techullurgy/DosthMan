package com.techullurgy.dosthman.shared.uimodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class EditableKeyValueItem(
    val id: Long, // Stable key for LazyColumn; excluded from equality
    initialKey: String = "",
    initialValue: String = "",
    initialEnabled: Boolean = true
) {
    val key = TextFieldState(initialKey)
    val value = TextFieldState(initialValue)
    var enabled by mutableStateOf(initialEnabled)

    fun matches(saved: KeyValueItem): Boolean =
        enabled == saved.enabled &&
                key.text.contentEquals(saved.key) &&
                value.text.contentEquals(saved.value)

    fun toDomain() = KeyValueItem(
        key = key.text.toString(),
        value = value.text.toString(),
        enabled = enabled
    )
}