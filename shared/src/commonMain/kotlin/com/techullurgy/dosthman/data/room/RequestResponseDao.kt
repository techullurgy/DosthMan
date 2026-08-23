package com.techullurgy.dosthman.data.room

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestResponseDao {

    @Query("SELECT * FROM request_response")
    fun getAllRequestResponses(): Flow<List<RequestResponseEntity>>

    @Query("SELECT * FROM request_response WHERE requestId = :requestId")
    suspend fun findByRequestId(requestId: String): RequestResponseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(data: RequestResponseEntity)

    @Delete
    suspend fun delete(data: RequestResponseEntity)
}