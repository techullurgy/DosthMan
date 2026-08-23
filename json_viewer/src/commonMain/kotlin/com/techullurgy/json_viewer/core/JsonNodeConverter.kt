package com.techullurgy.json_viewer.core

import kotlinx.serialization.json.*

internal fun String.toJsonNode(): JsonNode =
    Json.parseToJsonElement(this).toJsonNode()

internal fun JsonElement.toJsonNode(): JsonNode =
    when (this) {
        is JsonObject -> objectNode {
            entries.map { (key, value) ->
                key to value.toJsonNode()
            }
        }

        is JsonArray -> arrayNode {
            map { it.toJsonNode() }
        }

        is JsonPrimitive -> {
            when {
                isString -> stringNode { content }

                content.toIntOrNull() != null ->
                    intNode { content.toInt() }

                content.toLongOrNull() != null ->
                    longNode { content.toLong() }

                content.toDoubleOrNull() != null ->
                    doubleNode { content.toDouble() }

                else ->
                    stringNode { content }
            }
        }
    }