package com.techullurgy.dosthman.domain

import com.techullurgy.dosthman.domain.models.HttpRequest
import com.techullurgy.dosthman.domain.models.HttpResponse

interface NetworkCaller {
    suspend fun httpRequest(request: HttpRequest): Result<HttpResponse>
}
