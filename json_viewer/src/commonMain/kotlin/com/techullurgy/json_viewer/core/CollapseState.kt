package com.techullurgy.json_viewer.core

import androidx.compose.ui.text.AnnotatedString

internal data class CollapseState(
    val nodeId: NodeId,
    val start: Int,
    val end: Int,
    val isCollapsed: Boolean = false
)

// Step - 2
internal fun AnnotatedString.toCollapseStates(): List<CollapseState> {
    val collapsableRanges = getStringAnnotations(tag = COLLAPSIBLE, start = 0, end = length)

    return collapsableRanges.map {
        val startLine = lineForIndex(it.start)
        val endLine = lineForIndex(it.end - 1)
        val nodeId = NodeId(it.item)

        CollapseState(
            nodeId = nodeId,
            start = startLine,
            end = endLine,
            isCollapsed = false
        )
    }
}

fun AnnotatedString.lineForIndex(index: Int): Int = text.take(index).count { it == '\n' } + 1
