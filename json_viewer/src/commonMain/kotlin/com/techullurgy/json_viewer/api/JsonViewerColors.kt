package com.techullurgy.json_viewer.api

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class JsonViewerColors(
    val quoteColor: Color = Color.Unspecified,
    val colonColor: Color = Color.Unspecified,
    val keyTextColor: Color = Color.Unspecified,
    val valueNumberColor: Color = Color.Unspecified,
    val valueStringColor: Color = Color.Unspecified,
    val valueNullColor: Color = Color.Unspecified,
    val objectBracketColor: Color = Color.Unspecified,
    val arrayBracketColor: Color = Color.Unspecified,
    val collapseTriggerColor: Color = Color.Unspecified,
    val expandTriggerColor: Color = Color.Unspecified,
    val lineNumberColor: Color = Color.Unspecified,
    val commaColor: Color = Color.Unspecified,
    val gutterContainerColor: Color = Color.Unspecified,
    val jsonContainerColor: Color = Color.Unspecified,
)