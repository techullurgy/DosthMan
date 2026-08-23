package com.techullurgy.json_viewer.core

import kotlin.jvm.JvmInline

@JvmInline
internal value class NodeId(val id: String) {
    companion object {
        fun random() = NodeId(
            id = buildString {
                repeat(50) {
                    append((('a'..'z') + ('A'..'Z') + ('0'..'9')).random())
                }
            }
        )
    }
}
