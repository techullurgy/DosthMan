package com.techullurgy.dosthman.shared.uimodels

import androidx.compose.runtime.Stable
import com.techullurgy.dosthman.domain.models.ContentType


@Stable
internal sealed interface UiHttpResponse {

    @Stable
    data class Failure(val message: String): UiHttpResponse

    @Stable
    data class Data(
        val requestKey: String,
        val statusCode: UiHttpStatusCode,
        val requestHeaders: Map<String, List<String>>,
        val responseHeaders: Map<String, List<String>>,
        val timeTaken: Long,
        val content: String,
        val contentType: ContentType
    ): UiHttpResponse
}