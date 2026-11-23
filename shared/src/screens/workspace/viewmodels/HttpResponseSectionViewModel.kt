package com.techullurgy.dosthman.shared.screens.workspace.viewmodels

import androidx.compose.runtime.computedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.techullurgy.dosthman.domain.models.HttpResponse
import com.techullurgy.dosthman.domain.HttpRequestResponseManager
import com.techullurgy.dosthman.shared.uimodels.UiHttpResponse
import com.techullurgy.dosthman.shared.uimodels.UiHttpStatusCode
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel


@KoinViewModel
internal class HttpResponseSectionViewModel(
    @InjectedParam private val requestKey: String,
    private val httpRequestResponseManager: HttpRequestResponseManager,
) : ViewModel() {

    val isLoading: Boolean by computedStateOf {
        httpRequestResponseManager.ongoingRequests.containsKey(requestKey)
    }

    val response: UiHttpResponse? by computedStateOf {
        httpRequestResponseManager.executedResponses[requestKey]?.toUiHttpResponse(requestKey)
    }
}

private fun Result<HttpResponse>.toUiHttpResponse(requestKey: String): UiHttpResponse =
    fold(
        onSuccess = { httpResponse ->
            UiHttpResponse.Data(
                requestKey = requestKey,
                statusCode = UiHttpStatusCode(
                    code = httpResponse.statusCode,
                    text = httpResponse.statusMessage
                ),
                requestHeaders = httpResponse.requestHeaders,
                responseHeaders = httpResponse.responseHeaders,
                timeTaken = httpResponse.timeTaken,
                content = httpResponse.content,
                contentType = httpResponse.contentType,
            )
        },
        onFailure = { error ->
            UiHttpResponse.Failure(
                message = error.message ?: "Request failed",
            )
        },
    )