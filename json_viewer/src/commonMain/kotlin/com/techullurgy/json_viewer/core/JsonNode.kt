package com.techullurgy.json_viewer.core

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withAnnotation

internal inline fun arrayNode(block: () -> List<JsonNode>) = JsonNode.JsonArrayNode(block())
internal inline fun objectNode(block: () -> List<Pair<String, JsonNode>>) = JsonNode.JsonObjectNode(block())

internal inline fun stringNode(block: () -> String) = JsonNode.JsonPrimitiveNode.JsonStringNode(block())
internal inline fun intNode(block: () -> Int) = JsonNode.JsonPrimitiveNode.JsonIntNode(block())
internal inline fun longNode(block: () -> Long) = JsonNode.JsonPrimitiveNode.JsonLongNode(block())
internal inline fun doubleNode(block: () -> Double) = JsonNode.JsonPrimitiveNode.JsonDoubleNode(block())

internal fun JsonNode.renderWith(
    depth: Int = 0,
    spacesPerLevel: Int = 4,
    map: Map<NodeId, Boolean> = emptyMap()
): AnnotatedString {
    return toAnnotatedString(depth, spacesPerLevel, map)
}

internal sealed interface JsonNode {

    val nodeId: NodeId

    fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString

    sealed interface JsonPrimitiveNode<T>: JsonNode {
        val data: T

        data class JsonStringNode(
            override val data: String,
            override val nodeId: NodeId = NodeId.random()
        ): JsonPrimitiveNode<String> {
            override fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString {
                return buildAnnotatedString {
                    withTextAnnotation(QUOTE_TEXT) { append("\"") }
                    withTextAnnotation(VALUE_STRING_TEXT) { append(data) }
                    withTextAnnotation(QUOTE_TEXT) { append("\"") }
                }
            }
        }
        data class JsonIntNode(
            override val data: Int,
            override val nodeId: NodeId = NodeId.random()
        ): JsonPrimitiveNode<Int> {
            override fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString {
                return buildAnnotatedString {
                    withTextAnnotation(VALUE_NUMBER_TEXT) { append(data.toString()) }
                }
            }
        }
        data class JsonLongNode(
            override val data: Long,
            override val nodeId: NodeId = NodeId.random()
        ): JsonPrimitiveNode<Long> {
            override fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString {
                return buildAnnotatedString {
                    withTextAnnotation(VALUE_NUMBER_TEXT) { append(data.toString()) }
                }
            }
        }
        data class JsonDoubleNode(
            override val data: Double,
            override val nodeId: NodeId = NodeId.random()
        ): JsonPrimitiveNode<Double>  {
            override fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString {
                return buildAnnotatedString {
                    withTextAnnotation(VALUE_NUMBER_TEXT) { append(data.toString()) }
                }
            }
        }
    }

    data class JsonArrayNode(
        val values: List<JsonNode>,
        override val nodeId: NodeId = NodeId.random()
    ): JsonNode {
        override fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString {
            return if(values.isEmpty()) {
                buildAnnotatedString {
                    withTextAnnotation(ARRAY_BRACKET_TEXT) { append("[]") }
                }
            } else if (map[nodeId] ?: false) {
                buildAnnotatedString {
                    withAnnotation(
                        tag = COLLAPSIBLE,
                        annotation = nodeId.id
                    ) {
                        append("[...]")
                    }
                }
            } else {
                val indent = " ".repeat(depth * spacesPerLevel)
                val childIndent = " ".repeat((depth + 1) * spacesPerLevel)

                buildAnnotatedString {
                    withAnnotation(
                        tag = COLLAPSIBLE,
                        annotation = nodeId.id
                    ) {
                        withTextAnnotation(ARRAY_BRACKET_TEXT) { append("[\n") }

                        values.forEachIndexed { index, item ->
                            append(childIndent)
                            append(
                                item.toAnnotatedString(
                                    depth = depth + 1,
                                    spacesPerLevel = spacesPerLevel,
                                    map = map
                                )
                            )

                            if (index < values.lastIndex) {
                                withTextAnnotation(COMMA_TEXT) { append(",") }
                            }

                            append("\n")
                        }

                        append(indent)
                        withTextAnnotation(ARRAY_BRACKET_TEXT) { append("]") }
                    }
                }
            }
        }
    }

    data class JsonObjectNode(
        val values: List<Pair<String, JsonNode>>,
        override val nodeId: NodeId = NodeId.random()
    ): JsonNode {
        override fun toAnnotatedString(depth: Int, spacesPerLevel: Int, map: Map<NodeId, Boolean>): AnnotatedString {
            return if(values.isEmpty()) {
                buildAnnotatedString {
                    withTextAnnotation(OBJECT_BRACKET_TEXT) { append("{}") }
                }
            } else if (map[nodeId] ?: false) {
                buildAnnotatedString {
                    withAnnotation(
                        tag = COLLAPSIBLE,
                        annotation = nodeId.id
                    ) {
                        append("{...}")
                    }
                }
            } else {
                val indent = " ".repeat(depth * spacesPerLevel)
                val childIndent = " ".repeat((depth + 1) * spacesPerLevel)

                buildAnnotatedString {
                    withAnnotation(
                        tag = COLLAPSIBLE,
                        annotation = nodeId.id
                    ) {
                        withTextAnnotation(OBJECT_BRACKET_TEXT) { append("{\n") }

                        values.forEachIndexed { index, (key, value) ->
                            append(childIndent)
                            withTextAnnotation(QUOTE_TEXT) { append("\"") }
                            withTextAnnotation(KEY_TEXT) { append(key) }
                            withTextAnnotation(QUOTE_TEXT) { append("\"") }
                            withTextAnnotation(COLON_TEXT) { append(":") }
                            append(" ")

                            append(
                                value.toAnnotatedString(
                                    depth = depth + 1,
                                    spacesPerLevel = spacesPerLevel,
                                    map = map
                                )
                            )

                            if (index < values.lastIndex) {
                                withTextAnnotation(COMMA_TEXT) { append(",") }
                            }

                            append("\n")
                        }

                        append(indent)
                        withTextAnnotation(OBJECT_BRACKET_TEXT) { append("}") }
                    }
                }
            }
        }
    }
}

private inline fun <R: Any> AnnotatedString.Builder.withTextAnnotation(
    tag: String,
    crossinline block: AnnotatedString.Builder.() -> R
) {
    withAnnotation(
        tag = tag,
        annotation = "",
        block = block
    )
}