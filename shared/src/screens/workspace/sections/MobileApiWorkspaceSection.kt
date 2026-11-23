package com.techullurgy.dosthman.shared.screens.workspace.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.ViewModelStoreProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import com.techullurgy.dosthman.shared.components.EditableText
import com.techullurgy.dosthman.shared.screens.workspace.components.EmptyWorkspaceContent
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestTabViewModel
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpResponseSectionViewModel
import com.techullurgy.dosthman.shared.screens.workspace.core.LocalRequestsTabsSynchronizer
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestSectionViewModel
import com.techullurgy.dosthman.shared.screens.workspace.components.WorkspaceTabBar
import com.techullurgy.dosthman.shared.screens.workspace.components.WorkspaceUrlBar
import com.techullurgy.dosthman.shared.uimodels.UiHttpResponse
import kotlin.time.Clock
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private enum class MobileWorkspaceTab(val label: String) {
    Request("Request"),
    Response("Response"),
}

@Composable
internal fun MobileApiWorkspaceSection(
    requestTabsViewModelStoreProvider: ViewModelStoreProvider,
    requestTabsSaveableStateHolder: SaveableStateHolder,
    modifier: Modifier = Modifier,
) {
    val requestsTabsSynchronizer = LocalRequestsTabsSynchronizer.current

    fun closeTab(key: String) {
        requestsTabsSynchronizer.removeTab(key)

        requestTabsViewModelStoreProvider.clearKey(key)
        requestTabsSaveableStateHolder.removeState(key)
    }

    val selectedKey = requestsTabsSynchronizer.selectedTab

    Column(modifier = modifier.fillMaxSize()) {
        WorkspaceTabBar(
            openedTabs = requestsTabsSynchronizer.openedTabs,
            selectedTab = selectedKey,
            onSelectTab = { requestsTabsSynchronizer.openTab(it) },
            onCloseTab = ::closeTab,
            onAddTab = {
                val newKey = "${Clock.System.now().toEpochMilliseconds()}"
                requestsTabsSynchronizer.openTab(newKey)
            },
        )

        HorizontalDivider()

        if (selectedKey != null) {
            val viewModelStoreOwner = rememberViewModelStoreOwner(
                provider = requestTabsViewModelStoreProvider,
                key = selectedKey,
            )

            requestTabsSaveableStateHolder.SaveableStateProvider(selectedKey) {
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    val requestViewModel = koinViewModel<HttpRequestSectionViewModel> {
                        parametersOf(selectedKey)
                    }
                    val responseViewModel = koinViewModel<HttpResponseSectionViewModel> {
                        parametersOf(selectedKey)
                    }
                    val tabViewModel = koinViewModel<HttpRequestTabViewModel> {
                        parametersOf(selectedKey)
                    }

                    MobileTabWorkspace(
                        requestViewModel = requestViewModel,
                        responseViewModel = responseViewModel,
                        tabViewModel = tabViewModel,
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                    )
                }
            }
        } else {
            EmptyWorkspaceContent(
                onNewRequest = {
                    val newKey = "${Clock.System.now().toEpochMilliseconds()}"
                    requestsTabsSynchronizer.openTab(newKey)
                },
                onOpenRequest = { requestKey ->
                    requestsTabsSynchronizer.openTab(requestKey)
                },
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun MobileTabWorkspace(
    requestViewModel: HttpRequestSectionViewModel,
    responseViewModel: HttpResponseSectionViewModel,
    tabViewModel: HttpRequestTabViewModel,
    modifier: Modifier = Modifier,
) {
    val editableRequest = requestViewModel.editableHttpRequest
    var activeTab by rememberSaveable { mutableStateOf(MobileWorkspaceTab.Request) }

    Column(modifier = modifier) {
        if (editableRequest != null) {
            val requestName = tabViewModel.savedRequestName?.takeIf { it.isNotBlank() } ?: "Untitled Request"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                EditableText(
                    text = requestName,
                    onTextChange = { tabViewModel.saveName(it) },
                    textStyle = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
            }
            HorizontalDivider()

            WorkspaceUrlBar(
                editableHttpRequest = editableRequest,
                isSending = requestViewModel.isSending,
                isSaveable = tabViewModel.isSaveable,
                onSend = {
                    requestViewModel.send()
                    activeTab = MobileWorkspaceTab.Response
                },
                onSave = { tabViewModel.save() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
            HorizontalDivider()
        }

        PrimaryTabRow(selectedTabIndex = activeTab.ordinal) {
            MobileWorkspaceTab.entries.forEach { tab ->
                Tab(
                    selected = activeTab == tab,
                    onClick = { activeTab = tab },
                    text = {
                        val text = when (tab) {
                            MobileWorkspaceTab.Request -> {
                                val count = (editableRequest?.params?.size ?: 0) +
                                    (editableRequest?.headers?.size ?: 0)
                                if (count > 0) "Request ($count)" else "Request"
                            }
                            MobileWorkspaceTab.Response -> {
                                when (val resp = responseViewModel.response) {
                                    is UiHttpResponse.Data -> "Response (${resp.statusCode.code})"
                                    is UiHttpResponse.Failure -> "Response (Error)"
                                    null -> if (responseViewModel.isLoading) "Response (...)" else "Response"
                                }
                            }
                        }
                        Text(text)
                    },
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (activeTab) {
                MobileWorkspaceTab.Request -> RequestSection(
                    viewModel = requestViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
                MobileWorkspaceTab.Response -> ResponseSection(
                    viewModel = responseViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}