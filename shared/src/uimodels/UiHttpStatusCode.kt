package com.techullurgy.dosthman.shared.uimodels

import androidx.compose.runtime.Stable

@Stable
internal data class UiHttpStatusCode(
    val code: Int,
    val text: String
)