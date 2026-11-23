package com.techullurgy.dosthman.domain.models

data class HttpRequestResponse(
    val requestKey: String,
    val request: HttpRequest,
    val response: HttpResponse? = null
)