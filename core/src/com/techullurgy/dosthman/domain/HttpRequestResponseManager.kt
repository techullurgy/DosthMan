package com.techullurgy.dosthman.domain

import androidx.appstate.transform.listener
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.Snapshot
import com.techullurgy.dosthman.domain.models.HttpRequest
import com.techullurgy.dosthman.domain.models.HttpResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

@Singleton
class HttpRequestResponseManager(
    private val caller: NetworkCaller,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    scope: CoroutineScope = CoroutineScope(SupervisorJob()),
) {
    /**
     * It contains the currently ongoing (In-Flight) requests.
     */
    val ongoingRequests: Map<String, HttpRequest> field = mutableStateMapOf()

    /**
     * It contains the recent response, executed for the requestKey
     * */
    val executedResponses: Map<String, Result<HttpResponse>> field = mutableStateMapOf()

    private val activeJobs = mutableMapOf<String, Job>()

    init {
        scope.maintainActiveJobs()
    }

    fun request(requestKey: String, request: HttpRequest) {
        ongoingRequests[requestKey] = request
    }

    fun clear() {
        Snapshot.withMutableSnapshot {
            ongoingRequests.clear()
            executedResponses.clear()
        }
    }

    private fun CoroutineScope.maintainActiveJobs() {
        launch {
            try {
                listener {
                    val currentRequests = ongoingRequests.toMap()

                    // 1. Cancel and clean up removed keys
                    val removedKeys = activeJobs.keys - currentRequests.keys
                    for(key in removedKeys) {
                        activeJobs.remove(key)?.cancel()
                    }

                    // 2. Launch requests for newly added keys
                    val addedKeys = currentRequests.keys - activeJobs.keys
                    for(key in addedKeys) {
                        val request = currentRequests[key] ?: continue
                        activeJobs[key] = launch(ioDispatcher) {
                            performRequest(requestKey = key, request = request)
                        }
                    }
                }
            } finally {
                activeJobs.clear()
            }
        }
    }

    private suspend fun performRequest(requestKey: String, request: HttpRequest) {
        caller.httpRequest(request)
            .onSuccess { response ->
                Snapshot.withMutableSnapshot {
                    if(ongoingRequests.containsKey(requestKey)) {
                        executedResponses[requestKey] = Result.success(response)
                        ongoingRequests.remove(requestKey)
                    }
                }
            }
            .onFailure { error ->
                // Prevent the failed key from staying stuck in ongoingRequests
                Snapshot.withMutableSnapshot {
                    executedResponses[requestKey] = Result.failure(error)
                    ongoingRequests.remove(requestKey)
                }
            }
    }
}