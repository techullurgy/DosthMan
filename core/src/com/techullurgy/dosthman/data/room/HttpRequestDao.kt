package com.techullurgy.dosthman.data.room

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HttpRequestDao {
    @Query("SELECT * FROM HttpRequestEntity WHERE requestKey = :requestKey")
    fun savedRequest(requestKey: String): Flow<HttpRequestEntity?>

    @Query("SELECT * FROM HttpRequestEntity WHERE requestKey = :requestKey")
    suspend fun getRequest(requestKey: String): HttpRequestEntity?

    @Query("SELECT name FROM HttpRequestEntity WHERE requestKey = :requestKey")
    fun savedRequestName(requestKey: String): Flow<String?>

    @Query("SELECT * FROM HttpRequestEntity")
    fun allRequests(): Flow<List<HttpRequestEntity>>

    @Upsert
    suspend fun upsert(entity: HttpRequestEntity)

    @Query("""
        UPDATE HttpRequestEntity
        SET request = :request
        WHERE requestKey = :requestKey
    """)
    suspend fun updateRequest(requestKey: String, request: HttpRequestStored): Int

    @Query("""
        UPDATE HttpRequestEntity
        SET name = :name
        WHERE requestKey = :requestKey
    """)
    suspend fun updateRequestName(requestKey: String, name: String?): Int

    @Transaction
    suspend fun saveRequest(
        requestKey: String,
        request: HttpRequestStored,
        defaultName: String? = null
    ) {
        val updatedRows = updateRequest(requestKey, request)
        if (updatedRows == 0) {
            upsert(HttpRequestEntity(requestKey = requestKey, name = defaultName, request = request))
        }
    }

    @Transaction
    suspend fun saveRequestName(
        requestKey: String,
        name: String?,
        defaultRequest: HttpRequestStored
    ) {
        val updatedRows = updateRequestName(requestKey, name)
        if (updatedRows == 0) {
            upsert(HttpRequestEntity(requestKey = requestKey, name = name, request = defaultRequest))
        }
    }

    @Query("DELETE FROM HttpRequestEntity WHERE requestKey = :requestKey")
    suspend fun deleteRequest(requestKey: String): Int
}