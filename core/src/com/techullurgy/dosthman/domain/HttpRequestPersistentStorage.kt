package com.techullurgy.dosthman.domain

import com.techullurgy.dosthman.domain.models.HttpRequest
import com.techullurgy.dosthman.domain.models.SavedHttpRequest
import kotlinx.coroutines.flow.Flow

interface HttpRequestPersistentStorage {
    fun savedRequest(requestKey: String): Flow<HttpRequest?>
    fun savedRequestName(requestKey: String): Flow<String?>
    fun savedRequestDetails(requestKey: String): Flow<SavedHttpRequest?>
    fun allSavedRequests(): Flow<List<SavedHttpRequest>>
    suspend fun save(requestKey: String, httpRequest: HttpRequest)
    suspend fun saveName(requestKey: String, name: String)
    suspend fun delete(requestKey: String)
}