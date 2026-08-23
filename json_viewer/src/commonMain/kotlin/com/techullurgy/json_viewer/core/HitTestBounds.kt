package com.techullurgy.json_viewer.core

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

internal data class HitTestBounds<T>(
    private val rect: Rect,
    val onTapped: () -> T
) {
    fun contains(point: Offset): Boolean {
        return rect.contains(point)
    }
}