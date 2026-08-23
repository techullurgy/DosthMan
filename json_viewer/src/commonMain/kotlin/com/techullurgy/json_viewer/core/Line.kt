package com.techullurgy.json_viewer.core

import androidx.compose.ui.text.TextLayoutResult

internal data class Line(
    val nodeId: NodeId?,
    val collapseTriggerState: CollapseTriggerState,
    val jsonLine: TextLayoutResult,
    val lineNoLine: TextLayoutResult
)