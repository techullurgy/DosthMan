package com.techullurgy.json_viewer.core

import androidx.compose.ui.text.AnnotatedString

internal data class JsonState(
    val collapseStates: List<CollapseState> = emptyList(),
    val jsonLines: List<AnnotatedString> = emptyList()
)