package com.techullurgy.dosthman.domain.models

data class RequestResponse(
    val requestKey: String,
    val request: AppHttpRequest,
    val response: AppHttpResponse?,
    val isSavePending: Boolean,
    val isLoading: Boolean
)