package com.techullurgy.dosthman.data.room

import androidx.room3.ColumnTypeConverter
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.techullurgy.dosthman.domain.models.HttpMethod
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Entity
data class HttpRequestEntity(
    @PrimaryKey val requestKey: String,
    val name: String?,
    val request: HttpRequestStored
)

@Serializable
data class HttpRequestStored(
    val method: HttpMethod,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val params: Map<String, String> = emptyMap(),
    val body: String? = null,
)

object HttpRequestStoredTypeConverter {
    @ColumnTypeConverter
    fun serialize(value: HttpRequestStored): String = Json.encodeToString(value)
    @ColumnTypeConverter
    fun deserialize(value: String): HttpRequestStored = Json.decodeFromString(value)
}