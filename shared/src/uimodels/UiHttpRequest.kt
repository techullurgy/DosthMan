package com.techullurgy.dosthman.shared.uimodels

import com.techullurgy.dosthman.domain.models.HttpMethod
import com.techullurgy.dosthman.domain.models.HttpRequest

data class UiHttpRequest(
    val url: String = "",
    val method: UiHttpMethod = UiHttpMethod.Get,
    val headers: List<KeyValueItem> = emptyList(),
    val params: List<KeyValueItem> = emptyList(),
    val body: RequestBody = RequestBody.None
)

fun UiHttpRequest.toHttpRequest(): HttpRequest = HttpRequest(
    url = url,
    method = when(method) {
        UiHttpMethod.Get -> HttpMethod.Get
        UiHttpMethod.Post -> HttpMethod.Post
        UiHttpMethod.Put -> HttpMethod.Put
        UiHttpMethod.Patch -> HttpMethod.Patch
        UiHttpMethod.Delete -> HttpMethod.Delete
        UiHttpMethod.Head -> HttpMethod.Head
        UiHttpMethod.Options -> HttpMethod.Options
    },
    headers = headers.associate { it.key to it.value },
    params = params.associate { it.key to it.value },
    body = when(body) {
        RequestBody.None -> null
        is RequestBody.Text -> body.content
    }
)

fun HttpRequest.toUiHttpRequest(): UiHttpRequest = UiHttpRequest(
    url = url,
    method = when(method) {
        HttpMethod.Get -> UiHttpMethod.Get
        HttpMethod.Post -> UiHttpMethod.Post
        HttpMethod.Put -> UiHttpMethod.Put
        HttpMethod.Patch -> UiHttpMethod.Patch
        HttpMethod.Delete -> UiHttpMethod.Delete
        HttpMethod.Head -> UiHttpMethod.Head
        HttpMethod.Options -> UiHttpMethod.Options
    },
    headers = headers.map { [key, value] -> KeyValueItem(key, value) },
    params = params.map { [key, value] -> KeyValueItem(key, value) },
    body = if(body == null) RequestBody.None else RequestBody.Text(body!!)
)