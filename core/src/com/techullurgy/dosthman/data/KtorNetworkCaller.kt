package com.techullurgy.dosthman.data

import com.techullurgy.dosthman.domain.NetworkCaller
import com.techullurgy.dosthman.domain.models.HttpMethod
import com.techullurgy.dosthman.domain.models.HttpRequest
import com.techullurgy.dosthman.domain.models.HttpResponse
import com.techullurgy.dosthman.domain.models.ContentType as AppContentType
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.util.toMap
import org.koin.core.annotation.Singleton

@Singleton
internal class KtorNetworkCaller (
    private val httpClient: HttpClient = HttpClient(CIO),
): NetworkCaller {
    override suspend fun httpRequest(request: HttpRequest): Result<HttpResponse> {
        return runCatching {
            httpClient.request {
                url(request.url)
                method = when(request.method) {
                    HttpMethod.Get -> io.ktor.http.HttpMethod.Get
                    HttpMethod.Post -> io.ktor.http.HttpMethod.Post
                    HttpMethod.Delete -> io.ktor.http.HttpMethod.Delete
                    HttpMethod.Put -> io.ktor.http.HttpMethod.Put
                    HttpMethod.Patch -> io.ktor.http.HttpMethod.Patch
                    HttpMethod.Options -> io.ktor.http.HttpMethod.Options
                    HttpMethod.Head -> io.ktor.http.HttpMethod.Head
                }
                parameters {
                    request.params.forEach { (key, value) ->
                        set(key, value)
                    }
                }

                headers {
                    request.headers.forEach { (key, value) ->
                        set(key, value)
                    }
                }

                if(request.body != null) {
                    //contentType(ContentType.Application.Json)
                    setBody(request.body)
                }
            }.let {
                HttpResponse(
                    statusCode = it.status.value,
                    statusMessage = it.status.description,
                    requestHeaders = it.request.headers.toMap(),
                    responseHeaders = it.headers.toMap(),
                    timeTaken = it.responseTime.timestamp - it.requestTime.timestamp,
                    content = it.bodyAsText(),
                    contentType = when {
                        it.contentType()?.toString()?.contains(ContentType.Application.Json.toString()) == true -> AppContentType.ApplicationJson
                        else -> AppContentType.TextPlain
                    }
                )
            }
        }
    }
}