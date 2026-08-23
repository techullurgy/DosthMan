package com.techullurgy.json_viewer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Constraints
import com.techullurgy.json_viewer.api.LocalJsonViewerConfiguration
import com.techullurgy.json_viewer.core.Line

@Composable
internal fun JsonArea(
    linesProvider: () -> List<Line>,
    widthProvider: () -> Int,
    heightProvider: () -> Int,
    translateProvider: () -> Offset,
    modifier: Modifier = Modifier
) {
    val configuration = LocalJsonViewerConfiguration.current

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
                    color = configuration.colors.jsonContainerColor
                )
            }
    ) {
        drawJson(
            lines = linesProvider(),
            translate = translateProvider()
        )
    }
}

private fun DrawScope.drawJson(
    lines: List<Line>,
    translate: Offset
) {
    translate(
        top = translate.y,
        left = translate.x
    ) {
        var y = 0f
        lines.forEach { line ->
            drawText(
                textLayoutResult = line.jsonLine,
                topLeft = Offset(x = 0f, y = y)
            )
            y += maxOf(line.jsonLine.size.height, line.lineNoLine.size.height)
        }
    }
}
