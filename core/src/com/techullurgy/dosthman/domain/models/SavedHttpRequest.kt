package com.techullurgy.dosthman.domain.models

data class SavedHttpRequest(
    val requestKey: String,
    val name: String?,
    val request: HttpRequest
)
