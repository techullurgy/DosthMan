package com.techullurgy.dosthman.shared.screens.workspace.viewmodels

import androidx.compose.runtime.computedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.techullurgy.dosthman.domain.HttpRequestResponseManager
import com.techullurgy.dosthman.shared.HttpRequestDraftStore
import com.techullurgy.dosthman.shared.uimodels.EditableHttpRequest
import com.techullurgy.dosthman.shared.uimodels.toHttpRequest
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HttpRequestSectionViewModel(
    @InjectedParam private val requestKey: String,
    private val httpRequestDraftStore: HttpRequestDraftStore,
    private val httpRequestResponseManager: HttpRequestResponseManager,
) : ViewModel() {

    val editableHttpRequest: EditableHttpRequest? by computedStateOf {
        httpRequestDraftStore.drafts[requestKey]
    }

    val isSending: Boolean by computedStateOf {
        httpRequestResponseManager.ongoingRequests.containsKey(requestKey)
    }

    fun send() {
        val current = editableHttpRequest?.toUiHttpRequest() ?: return
        httpRequestResponseManager.request(
            requestKey = requestKey,
            request = current.toHttpRequest(),
        )
    }
}