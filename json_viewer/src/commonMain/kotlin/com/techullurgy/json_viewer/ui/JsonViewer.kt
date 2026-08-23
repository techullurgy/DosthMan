package com.techullurgy.json_viewer.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.techullurgy.json_viewer.api.JsonViewerConfiguration
import com.techullurgy.json_viewer.api.LocalJsonViewerConfiguration
import com.techullurgy.json_viewer.core.CollapseTriggerState
import com.techullurgy.json_viewer.core.JsonState
import com.techullurgy.json_viewer.core.Line
import com.techullurgy.json_viewer.core.mergeWithDefaultJsonViewerConfiguration

@Composable
fun JsonViewer(
    json: String,
    modifier: Modifier = Modifier,
    configuration: JsonViewerConfiguration = LocalJsonViewerConfiguration.current
) {
    val viewModel = viewModel(key = json) {
        JsonViewerViewModel(json)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    val textMeasurer = rememberTextMeasurer()

    val mergedConfiguration = mergeWithDefaultJsonViewerConfiguration(configuration)

    val textStyle = TextStyle(
        fontSize = mergedConfiguration.style.fontSize,
        fontWeight = mergedConfiguration.style.jsonFontWeight
    )

    val lines by remember(textMeasurer) {
        derivedStateOf {
            convertToLines(state, textMeasurer, textStyle)
        }
    }

    val density = LocalDensity.current

    val collapseTriggerSize = mergedConfiguration.style.collapseExpandTriggerSizeDp.dp

    val jsonLinesMaxSize by remember {
        derivedStateOf {
            val width = try {
                lines.maxOf { it.jsonLine.size.width }
            } catch (_: NoSuchElementException) { 0 }
            val height = lines.sumOf { it.jsonLine.size.height }
            IntSize(width, height)
        }
    }

    val gutterAreaMaxSize by remember(density) {
        derivedStateOf {
            val width = try {
                lines.maxOf { it.lineNoLine.size.width } + with(density) { collapseTriggerSize.roundToPx() }
            } catch (_: NoSuchElementException) { 0 }
            val height = lines.sumOf { it.lineNoLine.size.height }
            IntSize(width, height)
        }
    }

    LaunchedEffect(viewModel,configuration) {
        viewModel.setConfiguration(configuration)
    }

    CompositionLocalProvider(
        LocalJsonViewerConfiguration provides mergedConfiguration
    ) {
        Box(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            ) {
                JsonArea(
                    linesProvider = { lines },
                    widthProvider = { jsonLinesMaxSize.width + gutterAreaMaxSize.width },
                    heightProvider = { maxOf(jsonLinesMaxSize.height, gutterAreaMaxSize.height) },
                    translateProvider = { Offset(gutterAreaMaxSize.width.toFloat(), 0f) }
                )
            }

            Box(
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
            ) {
                GutterArea(
                    linesProvider = { lines },
                    widthProvider = { gutterAreaMaxSize.width },
                    heightProvider = { gutterAreaMaxSize.height },
                    onCollapseExpandToggleTriggerClick = { nodeId ->
                        viewModel.toggleCollapseExpand(nodeId.id)
                    }
                )
            }
        }
    }
}

private fun convertToLines(
    state: JsonState,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle
): List<Line> {
    var lineNo = 1
    return state.jsonLines.map { jsonLine ->
        val collapseState = state.collapseStates.firstOrNull { lineNo == it.start }
        val collapseTriggerState = collapseState
            ?.let {
                if(it.isCollapsed) CollapseTriggerState.COLLAPSED
                else CollapseTriggerState.EXPANDED
            } ?: CollapseTriggerState.NONE

        Line(
            nodeId = collapseState?.nodeId,
            collapseTriggerState = collapseTriggerState,
            jsonLine = textMeasurer.measure(jsonLine, style = textStyle),
            lineNoLine = textMeasurer.measure(lineNo.toString(), style = textStyle)
        ).also {
            if(collapseState != null) {
                if(collapseState.isCollapsed) {
                    lineNo += (collapseState.end - collapseState.start)
                }
            }
            lineNo += 1
        }
    }
}