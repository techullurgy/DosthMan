package com.techullurgy.json_viewer.api

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Stable
data class JsonViewerStyle(
    val fontSize: TextUnit = TextUnit.Unspecified,
    val jsonFontWeight: FontWeight? = null,
    val lineNoFontWeight: FontWeight? = null,
    val collapseExpandTriggerSizeDp: Int = Int.MIN_VALUE
)