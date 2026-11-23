package com.techullurgy.dosthman.shared.uimodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList

class EditableHttpRequest(initial: UiHttpRequest) {
    private var nextId = 0L

    var method by mutableStateOf(initial.method)
    val url = TextFieldState(initial.url)
    val textBody = TextFieldState((initial.body as? RequestBody.Text)?.content.orEmpty())

    val headers = initial.headers.map {
        EditableKeyValueItem(nextId++, it.key, it.value, it.enabled)
    }.toMutableStateList()

    val params = initial.params.map {
        EditableKeyValueItem(nextId++, it.key, it.value, it.enabled)
    }.toMutableStateList()

    fun addHeader() {
        headers.add(EditableKeyValueItem(nextId++))
    }

    fun removeHeader(id: Long) {
        headers.removeAll { it.id == id }
    }

    fun addParam() {
        params.add(EditableKeyValueItem(nextId++))
    }

    fun removeParam(id: Long) {
        params.removeAll { it.id == id }
    }

    fun matches(saved: UiHttpRequest): Boolean {
        if (method != saved.method) return false
        if (!url.text.contentEquals(saved.url)) return false
        if (!matchesList(headers, saved.headers)) return false
        if (!matchesList(params, saved.params)) return false

        if(when (val savedBody = saved.body) {
                RequestBody.None -> textBody.text.isEmpty()
                is RequestBody.Text -> textBody.text.contentEquals(savedBody.content)
            }.not()) return false

        return true
    }

    fun toUiHttpRequest(): UiHttpRequest = UiHttpRequest(
        method = method,
        url = url.text.toString(),
        headers = headers.map { it.toDomain() },
        params = params.map { it.toDomain() },
        body = if (textBody.text.isEmpty()) RequestBody.None else RequestBody.Text(textBody.text.toString())
    )

    private fun matchesList(
        editable: List<EditableKeyValueItem>,
        saved: List<KeyValueItem>
    ): Boolean {
        if (editable.size != saved.size) return false
        for ([i, element] in editable.withIndex()) {
            if (!element.matches(saved[i])) return false
        }
        return true
    }
}