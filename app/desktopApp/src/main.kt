package com.techullurgy.dosthman

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.techullurgy.dosthman.shared.App

fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        size = DpSize(1200.dp, 800.dp),
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "DosthMan",
        state = windowState
    ) {
        App()
    }
}
