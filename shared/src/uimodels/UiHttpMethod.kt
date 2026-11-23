package com.techullurgy.dosthman.shared.uimodels

import androidx.compose.ui.graphics.Color
import com.techullurgy.dosthman.shared.theme.ColorToken

enum class UiHttpMethod(val color: Color) {
    Get(ColorToken.Green300.color),
    Post(ColorToken.Yellow300.color),
    Put(ColorToken.Teal200.color),
    Patch(ColorToken.Teal500.color),
    Delete(ColorToken.Red500.color),
    Head(ColorToken.Purple500.color),
    Options(ColorToken.Blue300.color)
}