package com.techullurgy.dosthman.shared.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.TraversableNode
import androidx.compose.ui.platform.InspectorInfo

fun Modifier.preserveFocusOnTapBounds(enabled: Boolean = true): Modifier =
    this then PreserveFocusOnTapBoundsElement(enabled)

private data class PreserveFocusOnTapBoundsElement(
    val enabled: Boolean,
) : ModifierNodeElement<PreserveFocusOnTapBoundsNode>() {

    override fun create() = PreserveFocusOnTapBoundsNode(enabled)

    override fun update(node: PreserveFocusOnTapBoundsNode) {
        node.enabled = enabled
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "registerTextFieldBounds"
        properties["enabled"] = enabled
    }
}

internal class PreserveFocusOnTapBoundsNode(
    var enabled: Boolean,
) : Modifier.Node(),
    TraversableNode,
    LayoutAwareModifierNode {

    override val traverseKey: Any = PreserveFocusOnTapBoundsTraverseKey

    private var coordinates: LayoutCoordinates? = null

    override fun onPlaced(coordinates: LayoutCoordinates) {
        this.coordinates = coordinates
    }

    override fun onDetach() {
        coordinates = null
    }

    fun containsRootPoint(rootPoint: Offset): Boolean {
        if (!enabled) return false
        val coords = coordinates?.takeIf { it.isAttached } ?: return false
        return coords.boundsInRoot().contains(rootPoint)
    }
}

internal data object PreserveFocusOnTapBoundsTraverseKey
