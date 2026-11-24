package com.techullurgy.dosthman.shared.screens.workspace.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.ViewModelStoreProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import com.techullurgy.dosthman.shared.components.EditableText
import com.techullurgy.dosthman.shared.screens.workspace.components.EmptyWorkspaceContent
import com.techullurgy.dosthman.shared.screens.workspace.components.WorkspaceSidebar
import com.techullurgy.dosthman.shared.screens.workspace.components.WorkspaceTabBar
import com.techullurgy.dosthman.shared.screens.workspace.components.WorkspaceUrlBar
import com.techullurgy.dosthman.shared.screens.workspace.core.LocalRequestsTabsSynchronizer
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestSectionViewModel
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestTabViewModel
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpResponseSectionViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock

private enum class WorkspaceArea {
    SideBar, Main
}

@OptIn(ExperimentalGridApi::class)
@Composable
internal fun DesktopApiWorkspaceSection(
    requestTabsViewModelStoreProvider: ViewModelStoreProvider,
    requestTabsSaveableStateHolder: SaveableStateHolder,
    modifier: Modifier = Modifier,
) {
    Grid(
        config = {
            val sidebarWidthPercentage = minOf(
                constraints.maxWidth * 0.25f,
                300.dp.toPx()
            ) / constraints.maxWidth

            row(1f)
            column(sidebarWidthPercentage)
            column(1f - sidebarWidthPercentage)

            area(
                WorkspaceArea.SideBar,
                row = 1,
                column = 1
            )

            area(
                WorkspaceArea.Main,
                row = 1,
                column = 2
            )
        },
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.gridItem(WorkspaceArea.SideBar).fillMaxSize()
        ) {
            WorkspaceSidebar()
        }

        Box(Modifier.gridItem(WorkspaceArea.Main).fillMaxSize()) {
            DesktopWorkspaceMain(
                requestTabsViewModelStoreProvider = requestTabsViewModelStoreProvider,
                requestTabsSaveableStateHolder = requestTabsSaveableStateHolder,
            )
        }
    }
}

@Composable
private fun DesktopWorkspaceMain(
    requestTabsViewModelStoreProvider: ViewModelStoreProvider,
    requestTabsSaveableStateHolder: SaveableStateHolder,
) {
    val requestsTabsSynchronizer = LocalRequestsTabsSynchronizer.current

    fun closeTab(key: String) {
        requestsTabsSynchronizer.removeTab(key)

        requestTabsViewModelStoreProvider.clearKey(key)
        requestTabsSaveableStateHolder.removeState(key)
    }

    val selectedKey = requestsTabsSynchronizer.selectedTab

    Column(Modifier.fillMaxSize()) {
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
                key = selectedKey,
                provider = requestTabsViewModelStoreProvider,
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

                    DesktopTabWorkspace(
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
                showSavedRequests = false,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun DesktopTabWorkspace(
    requestViewModel: HttpRequestSectionViewModel,
    responseViewModel: HttpResponseSectionViewModel,
    tabViewModel: HttpRequestTabViewModel,
    modifier: Modifier = Modifier,
) {
    val editableRequest = requestViewModel.editableHttpRequest

    Column(modifier = modifier) {
        if (editableRequest != null) {
            val requestName = tabViewModel.savedRequestName?.takeIf { it.isNotBlank() } ?: "Untitled Request"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
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
                onSend = { requestViewModel.send() },
                onSave = { tabViewModel.save() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            HorizontalDivider()
        }

        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                RequestSection(
                    viewModel = requestViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            VerticalDivider()

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                ResponseSection(
                    viewModel = responseViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}