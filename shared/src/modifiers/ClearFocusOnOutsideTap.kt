package com.techullurgy.dosthman.shared.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.TraversableNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.traverseDescendants
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize

fun Modifier.clearFocusOnOutsideTap(): Modifier = this then ClearFocusOnOutsideTapElement

private data object ClearFocusOnOutsideTapElement : ModifierNodeElement<ClearFocusOnOutsideTapNode>() {

    override fun create() = ClearFocusOnOutsideTapNode()
    override fun update(node: ClearFocusOnOutsideTapNode) = Unit

    override fun InspectorInfo.inspectableProperties() {
        name = "clearFocusOnOutsideTap"
    }
}

private class ClearFocusOnOutsideTapNode : Modifier.Node(),
    PointerInputModifierNode,
    LayoutAwareModifierNode,
    CompositionLocalConsumerModifierNode
{
    private var coordinates: LayoutCoordinates? = null

    override fun onPlaced(coordinates: LayoutCoordinates) {
        this.coordinates = coordinates
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize,
    ) {
        if (pass != PointerEventPass.Final || pointerEvent.type != PointerEventType.Press) return

        val down = pointerEvent.changes.firstOrNull { it.changedToDownIgnoreConsumed() } ?: return
        val rootPoint = coordinates
            ?.takeIf { it.isAttached }
            ?.localToRoot(down.position)
            ?: down.position

        var isInsideAnyPreserveFocusOnTapBounds = false
        traverseDescendants(PreserveFocusOnTapBoundsTraverseKey) { node ->
            if (node is PreserveFocusOnTapBoundsNode && node.containsRootPoint(rootPoint)) {
                isInsideAnyPreserveFocusOnTapBounds = true
                TraversableNode.Companion.TraverseDescendantsAction.CancelTraversal
            } else {
                TraversableNode.Companion.TraverseDescendantsAction.ContinueTraversal
            }
        }

        if (!isInsideAnyPreserveFocusOnTapBounds) {
            currentValueOf(LocalFocusManager).clearFocus()
        }
    }

    override fun onCancelPointerInput() = Unit

    override fun onDetach() {
        coordinates = null
    }
}