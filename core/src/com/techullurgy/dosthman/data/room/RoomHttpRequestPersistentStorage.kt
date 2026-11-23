package com.techullurgy.dosthman.data.room

import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import com.techullurgy.dosthman.domain.models.HttpMethod
import com.techullurgy.dosthman.domain.models.HttpRequest
import com.techullurgy.dosthman.domain.models.SavedHttpRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Singleton

@Singleton
class RoomHttpRequestPersistentStorage(
    private val dao: HttpRequestDao
): HttpRequestPersistentStorage {
    override fun savedRequest(requestKey: String): Flow<HttpRequest?> {
        return dao.savedRequest(requestKey).map { entity ->
            entity?.let {
                HttpRequest(
                    method = it.request.method,
                    url = it.request.url,
                    headers = it.request.headers,
                    params = it.request.params,
                    body = it.request.body,
                )
            }
        }
    }

    override fun savedRequestName(requestKey: String): Flow<String?> {
        return dao.savedRequestName(requestKey)
    }

    override fun savedRequestDetails(requestKey: String): Flow<SavedHttpRequest?> {
        return dao.savedRequest(requestKey).map { entity ->
            entity?.let {
                SavedHttpRequest(
                    requestKey = it.requestKey,
                    name = it.name,
                    request = HttpRequest(
                        method = it.request.method,
                        url = it.request.url,
                        headers = it.request.headers,
                        params = it.request.params,
                        body = it.request.body,
                    )
                )
            }
        }
    }

    override fun allSavedRequests(): Flow<List<SavedHttpRequest>> {
        return dao.allRequests().map { list ->
            list.map { entity ->
                SavedHttpRequest(
                    requestKey = entity.requestKey,
                    name = entity.name,
                    request = HttpRequest(
                        method = entity.request.method,
                        url = entity.request.url,
                        headers = entity.request.headers,
                        params = entity.request.params,
                        body = entity.request.body,
                    )
                )
            }
        }
    }

    override suspend fun save(
        requestKey: String,
        httpRequest: HttpRequest
    ) {
        dao.saveRequest(
            requestKey = requestKey,
            request = HttpRequestStored(
                method = httpRequest.method,
                url = httpRequest.url,
                headers = httpRequest.headers,
                params = httpRequest.params,
                body = httpRequest.body,
            )
        )
    }

    override suspend fun saveName(
        requestKey: String,
        name: String
    ) {
        dao.saveRequestName(
            requestKey = requestKey,
            name = name,
            defaultRequest = HttpRequestStored(
                method = HttpMethod.Get,
                url = "",
            )
        )
    }

    override suspend fun delete(requestKey: String) {
        dao.deleteRequest(requestKey)
    }
}