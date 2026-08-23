package com.techullurgy.json_viewer.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.json_viewer.api.JsonViewerConfiguration
import com.techullurgy.json_viewer.core.CollapseState
import com.techullurgy.json_viewer.core.JsonState
import com.techullurgy.json_viewer.core.NodeId
import com.techullurgy.json_viewer.core.applyColors
import com.techullurgy.json_viewer.core.renderWith
import com.techullurgy.json_viewer.core.toCollapseStates
import com.techullurgy.json_viewer.core.toJsonNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class JsonViewerViewModel(
    jsonString: String
): ViewModel() {
    private val _collapseStates = MutableStateFlow<List<CollapseState>>(emptyList())
    private val configurationState = MutableStateFlow<JsonViewerConfiguration?>(null)

    private val _state = MutableStateFlow(JsonState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val jsonNode = jsonString.toJsonNode()
            val originalAnnotated = jsonNode.renderWith(map = emptyMap())
            _collapseStates.value = originalAnnotated.toCollapseStates()

            combine(
                _collapseStates,
                configurationState
            ) { states, configuration ->
                val map = states.associateBy(
                    keySelector = { it.nodeId },
                    valueTransform = { it.isCollapsed }
                )
                val modifiedAnnotated = jsonNode.renderWith(map = map).let {
                    if(configuration != null) {
                        it.applyColors(configuration.colors)
                    } else it
                }
                JsonState(
                    collapseStates = states,
                    jsonLines = modifiedAnnotated.lineSequenceAnnotated()
                )
            }.collectLatest {
                _state.value = it
            }
        }
    }

    // TODO("make it private")
    fun toggleCollapseExpand(id: String) {
        val nodeId = NodeId(id)
        _collapseStates.value = _collapseStates.value.map {
            if(it.nodeId == nodeId) {
                it.copy(isCollapsed = it.isCollapsed.not())
            } else it
        }
    }

    // TODO("make it private")
    fun setConfiguration(configuration: JsonViewerConfiguration) {
        configurationState.value = configuration
    }
}

private fun AnnotatedString.lineSequenceAnnotated(): List<AnnotatedString> {
    var start = 0
    return text.lineSequence().toList().map { line ->
        val end = start + line.length
        subSequence(start, end).also {
            start = end + 1
        }
    }
}