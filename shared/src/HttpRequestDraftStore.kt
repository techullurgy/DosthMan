package com.techullurgy.dosthman.shared

import androidx.compose.runtime.mutableStateMapOf
import com.techullurgy.dosthman.shared.uimodels.EditableHttpRequest
import org.koin.core.annotation.Singleton
import com.techullurgy.dosthman.shared.uimodels.UiHttpRequest

@Singleton
class HttpRequestDraftStore {
    val drafts: Map<String, EditableHttpRequest> field = mutableStateMapOf()

    fun getOrCreate(requestKey: String, initial: UiHttpRequest): EditableHttpRequest =
        drafts.getOrPut(requestKey) { EditableHttpRequest(initial) }

    fun reset(requestKey: String, httpRequest: UiHttpRequest) {
        drafts[requestKey] = EditableHttpRequest(httpRequest)
    }

    fun remove(requestKey: String) {
        drafts.remove(requestKey)
    }

    fun clear() {
        drafts.clear()
    }
}