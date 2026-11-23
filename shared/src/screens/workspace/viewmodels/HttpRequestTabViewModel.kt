package com.techullurgy.dosthman.shared.screens.workspace.viewmodels

import androidx.appstate.transform.transform
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.computedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import com.techullurgy.dosthman.shared.HttpRequestDraftStore
import com.techullurgy.dosthman.shared.uimodels.*
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HttpRequestTabViewModel(
    @InjectedParam private val requestKey: String,
    private val requestPersistentStorage: HttpRequestPersistentStorage,
    private val httpRequestDraftStore: HttpRequestDraftStore,
) : ViewModel() {

    val savedHttpRequest: UiHttpRequest? by transform(
        initialValue = null,
        scope = viewModelScope,
    ) {
        val requestFlow = remember(requestKey) {
            requestPersistentStorage.savedRequest(requestKey = requestKey)
                .distinctUntilChanged()
                .map { it?.toUiHttpRequest() }
        }
        requestFlow.collectAsState(initial = null).value
    }

    val savedRequestName: String? by transform(
        initialValue = null,
        scope = viewModelScope,
    ) {
        val nameFlow = remember(requestKey) {
            requestPersistentStorage.savedRequestName(requestKey = requestKey)
                .distinctUntilChanged()
        }
        nameFlow.collectAsState(initial = null).value
    }

    private val editableHttpRequest: EditableHttpRequest? by computedStateOf {
        httpRequestDraftStore.drafts[requestKey]
    }

    val isSaveable: Boolean by computedStateOf {
        val current = editableHttpRequest ?: return@computedStateOf false
        val saved = savedHttpRequest
        if (saved == null) {
            current.url.text.isNotEmpty() ||
                current.headers.isNotEmpty() ||
                current.params.isNotEmpty() ||
                current.textBody.text.isNotEmpty() ||
                current.method != UiHttpMethod.Get
        } else {
            !current.matches(saved)
        }
    }

    init {
        viewModelScope.launch {
            val initialSaved = requestPersistentStorage.savedRequest(requestKey).first()
            val initialUi = initialSaved?.toUiHttpRequest() ?: UiHttpRequest()
            httpRequestDraftStore.getOrCreate(requestKey, initialUi)
        }
    }

    fun save() {
        val toSave = editableHttpRequest?.toUiHttpRequest()?.toHttpRequest() ?: return
        if (!isSaveable) return
        viewModelScope.launch {
            withContext(NonCancellable) {
                requestPersistentStorage.save(requestKey, toSave)
            }
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                requestPersistentStorage.saveName(requestKey, name)
            }
        }
    }

    override fun onCleared() {
        httpRequestDraftStore.remove(requestKey)
    }
}