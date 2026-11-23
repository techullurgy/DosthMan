package com.techullurgy.dosthman.shared.uimodels

sealed interface RequestBody {
    data object None : RequestBody
    data class Text(val content: String, val contentType: String = "application/json") : RequestBody
}