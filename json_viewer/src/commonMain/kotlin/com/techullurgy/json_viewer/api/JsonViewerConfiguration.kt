package com.techullurgy.json_viewer.api

import androidx.compose.runtime.Stable

@Stable
data class JsonViewerConfiguration(
    val colors: JsonViewerColors = JsonViewerColors(),
    val style: JsonViewerStyle = JsonViewerStyle()
)