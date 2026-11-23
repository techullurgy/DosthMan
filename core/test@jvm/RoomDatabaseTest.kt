package com.techullurgy.dosthman.data.room

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.dosthman.domain.models.HttpMethod
import com.techullurgy.dosthman.domain.models.HttpRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class RoomDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var storage: RoomHttpRequestPersistentStorage

    @BeforeTest
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder<AppDatabase>()
            .setDriver(BundledSQLiteDriver())
            .build()
        storage = RoomHttpRequestPersistentStorage(database.httpRequestDao())
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `savedRequest flow returns null when entity does not exist`() = runBlocking {
        val result = storage.savedRequest("non-existent-key").first()
        assertNull(result)
    }

    @Test
    fun `saving a request and observing request flow`() = runBlocking {
        val requestKey = "test-req-1"
        val request = HttpRequest(
            method = HttpMethod.Post,
            url = "https://example.com/api/test",
            headers = mapOf("Authorization" to "Bearer token123"),
            params = mapOf("query" to "test"),
            body = "{\"key\":\"value\"}"
        )

        // Save request
        storage.save(requestKey, request)

        // Observe request flow
        val saved = storage.savedRequest(requestKey).first()
        assertNotNull(saved)
        assertEquals(HttpMethod.Post, saved.method)
        assertEquals("https://example.com/api/test", saved.url)
        assertEquals("Bearer token123", saved.headers["Authorization"])
        assertEquals("test", saved.params["query"])
        assertEquals("{\"key\":\"value\"}", saved.body)
    }

    @Test
    fun `saving a name of the request and observing name flow`() = runBlocking {
        val requestKey = "test-req-2"
        val initialName = storage.savedRequestName(requestKey).first()
        assertNull(initialName)

        // Save name
        storage.saveName(requestKey, "My Custom Request Name")

        val updatedName = storage.savedRequestName(requestKey).first()
        assertEquals("My Custom Request Name", updatedName)
    }

    @Test
    fun `saving name preserves existing request and saving request preserves existing name`() = runBlocking {
        val requestKey = "test-req-3"
        val request = HttpRequest(
            method = HttpMethod.Get,
            url = "https://example.com/api/users"
        )

        // 1. Save request
        storage.save(requestKey, request)
        assertNull(storage.savedRequestName(requestKey).first())

        // 2. Save name - should preserve request
        storage.saveName(requestKey, "Get Users API")
        val savedReqAfterName = storage.savedRequest(requestKey).first()
        assertNotNull(savedReqAfterName)
        assertEquals("https://example.com/api/users", savedReqAfterName.url)
        assertEquals("Get Users API", storage.savedRequestName(requestKey).first())

        // 3. Update request - should preserve name
        val updatedRequest = request.copy(url = "https://example.com/api/users/v2")
        storage.save(requestKey, updatedRequest)
        assertEquals("Get Users API", storage.savedRequestName(requestKey).first())
        val finalReq = storage.savedRequest(requestKey).first()
        assertNotNull(finalReq)
        assertEquals("https://example.com/api/users/v2", finalReq.url)
    }

    @Test
    fun `savedRequestDetails flow and allSavedRequests flow`() = runBlocking {
        val requestKey = "test-req-4"
        storage.save(requestKey, HttpRequest(method = HttpMethod.Put, url = "https://example.com/item"))
        storage.saveName(requestKey, "Update Item")

        val details = storage.savedRequestDetails(requestKey).first()
        assertNotNull(details)
        assertEquals(requestKey, details.requestKey)
        assertEquals("Update Item", details.name)
        assertEquals(HttpMethod.Put, details.request.method)

        val all = storage.allSavedRequests().first()
        assertEquals(1, all.size)
        assertEquals("Update Item", all.first().name)
    }

    @Test
    fun `delete request removes from database`() = runBlocking {
        val requestKey = "test-req-5"
        storage.save(requestKey, HttpRequest(method = HttpMethod.Get, url = "https://example.com"))
        assertNotNull(storage.savedRequest(requestKey).first())

        storage.delete(requestKey)
        assertNull(storage.savedRequest(requestKey).first())
        assertNull(storage.savedRequestName(requestKey).first())
    }
}
