package com.techullurgy.dosthman.domain.models

data class HttpResponse(
    val statusCode: Int,
    val statusMessage: String,
    val requestHeaders: Map<String, List<String>>,
    val responseHeaders: Map<String, List<String>>,
    val timeTaken: Long,
    val content: String,
    val contentType: ContentType
)
