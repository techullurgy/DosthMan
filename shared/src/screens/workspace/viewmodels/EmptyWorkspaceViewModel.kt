package com.techullurgy.dosthman.shared.screens.workspace.viewmodels

import androidx.appstate.transform.transform
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import com.techullurgy.dosthman.domain.models.SavedHttpRequest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class EmptyWorkspaceViewModel(
    private val storage: HttpRequestPersistentStorage
): ViewModel() {
    val savedRequests by transform(initialValue = emptyList<SavedHttpRequest>(), scope = viewModelScope) {
        remember {
            storage.allSavedRequests()
                .distinctUntilChanged()
        }.collectAsState(emptyList()).value
    }
}