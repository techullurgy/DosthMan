package com.techullurgy.dosthman.shared.screens.workspace.components

import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch

private enum class SlidingAnchors {
    Open, Closed
}

@Composable
internal fun SlidingContainer(
    isOpen: Boolean,
    onIsOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    var drawerWidth by remember { mutableFloatStateOf(0f) }

    val anchorState = retain(drawerWidth) {
        AnchoredDraggableState(
            initialValue = SlidingAnchors.Closed,
            anchors = DraggableAnchors {
                SlidingAnchors.Closed at 0f
                SlidingAnchors.Open at (drawerWidth * 0.75f)
            }
        )
    }

    LaunchedEffect(isOpen) {
        val desiredTarget = if (isOpen) {
            SlidingAnchors.Open
        } else {
            SlidingAnchors.Closed
        }

        if (anchorState.settledValue != desiredTarget) {
            anchorState.animateTo(desiredTarget)
        }
    }

    LaunchedEffect(anchorState.settledValue) {
        if (anchorState.settledValue == SlidingAnchors.Closed) {
            onIsOpenChange(false)
        } else {
            onIsOpenChange(true)
        }
    }

    Box(
        modifier = Modifier
            .onSizeChanged {
                drawerWidth = it.width.toFloat()
            }
            .then(modifier)
            .graphicsLayer {
                translationX = anchorState.requireOffset()

                val fraction = if (drawerWidth > 0f) (anchorState.requireOffset() / drawerWidth).coerceIn(0f, 1f) else 0f
                val scale = lerp(1f, 0.8f, fraction)
                scaleX = scale
                scaleY = scale

                val percent = lerp(0, 20, fraction)
                shape = RoundedCornerShape(percent)
                clip = true
            }
            .shadow(
                elevation = if (anchorState.requireOffset() > 0f) 8.dp else 0.dp,
                shape = RoundedCornerShape(if (drawerWidth > 0f) lerp(0, 20, (anchorState.requireOffset() / drawerWidth).coerceIn(0f, 1f)) else 0)
            )
            .background(MaterialTheme.colorScheme.background)
            .anchoredDraggable(
                state = anchorState,
                orientation = Orientation.Horizontal,
                flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                    state = anchorState,
                    animationSpec = spring()
                ),
            )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }

        if (anchorState.settledValue == SlidingAnchors.Open) {
            Box(
                Modifier.matchParentSize()
                    .clickable(
                        onClick = {
                            scope.launch {
                                anchorState.animateTo(SlidingAnchors.Closed)
                            }
                        }
                    )
            )
        }
    }
}
