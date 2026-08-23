package com.techullurgy.json_viewer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.techullurgy.json_viewer.api.JsonViewerConfiguration
import com.techullurgy.json_viewer.api.LocalJsonViewerConfiguration
import com.techullurgy.json_viewer.core.CollapseTriggerState
import com.techullurgy.json_viewer.core.HitTestBounds
import com.techullurgy.json_viewer.core.Line
import com.techullurgy.json_viewer.core.NodeId

@Composable
internal fun GutterArea(
    linesProvider: () -> List<Line>,
    widthProvider: () -> Int,
    heightProvider: () -> Int,
    onCollapseExpandToggleTriggerClick: (NodeId) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalJsonViewerConfiguration.current

    val collapseTriggerHitTestBounds = remember {
        mutableStateListOf<HitTestBounds<NodeId>>()
    }

    Canvas(
        modifier = modifier
            .layout { m, _ ->
                val width = widthProvider()
                val height = heightProvider()
                val c = Constraints.fixed(width, height)
                val p = m.measure(c)
                layout(p.width, p.height) {
                    p.place(0, 0)
                }
            }
            .drawBehind {
                drawRect(
                    color = configuration.colors.gutterContainerColor
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { point ->
                    for(hitBound in collapseTriggerHitTestBounds) {
                        if(hitBound.contains(point)) {
                            val nodeId = hitBound.onTapped()
                            onCollapseExpandToggleTriggerClick(nodeId)
                            return@detectTapGestures
                        }
                    }
                }
            }
    ) {
        with(configuration) {
            drawGutterIconsWithLineNumbers(
                lines = linesProvider(),
                collapseTriggerHitTestBounds = collapseTriggerHitTestBounds
            )
        }
    }
}

context(configuration: JsonViewerConfiguration)
private fun DrawScope.drawGutterIconsWithLineNumbers(
    lines: List<Line>,
    collapseTriggerHitTestBounds: SnapshotStateList<HitTestBounds<NodeId>>
) {
    val collapseTriggerSize = configuration.style.collapseExpandTriggerSizeDp.dp

    collapseTriggerHitTestBounds.clear()
    var y = 0f
    lines.forEach { line ->
        translate(
            left = 0f,
            top = (y + line.lineNoLine.size.height) - (collapseTriggerSize.toPx() * 2f)
        ) {
            when(line.collapseTriggerState) {
                CollapseTriggerState.NONE -> {}
                CollapseTriggerState.COLLAPSED -> {
                    drawExpandTriangle(collapseTriggerSize.toPx(), configuration.colors.expandTriggerColor)
                }
                CollapseTriggerState.EXPANDED -> {
                    drawCollapseTriangle(collapseTriggerSize.toPx(), configuration.colors.collapseTriggerColor)
                }
            }
        }

        if(line.collapseTriggerState != CollapseTriggerState.NONE) {
            collapseTriggerHitTestBounds.add(
                HitTestBounds(
                    rect = Rect(
                        left = 0f,
                        top = y,
                        right = collapseTriggerSize.toPx(),
                        bottom = y + line.lineNoLine.size.height
                    ),
                    onTapped = { line.nodeId ?: error("NodeId not available") }
                )
            )
        }

        drawText(
            textLayoutResult = line.lineNoLine,
            topLeft = Offset(x = collapseTriggerSize.toPx(), y = y),
            color = configuration.colors.lineNumberColor
        )
        y += maxOf(line.jsonLine.size.height, line.lineNoLine.size.height)
    }
}

private fun DrawScope.drawCollapseTriangle(
    size: Float,
    color: Color
) {
    // v
    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(size, 0f)
        lineTo(size * .5f, size)
        close()
    }

    drawPath(
        path = path,
        color = color
    )
}

private fun DrawScope.drawExpandTriangle(
    size: Float,
    color: Color
) {
    // >
    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(size, size * .5f)
        lineTo(0f, size)
        close()
    }

    drawPath(
        path = path,
        color = color
    )
}