package com.techullurgy.dosthman.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.techullurgy.dosthman.shared.theme.LocalTextFieldColors

@Composable
internal fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = LocalTextFieldColors.current,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = RectangleShape,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val focused by interactionSource.collectIsFocusedAsState()

    val modifiedTextStyle = textStyle.merge(
        TextStyle(
            color = if(focused) colors.focusedTextColor else colors.unfocusedTextColor
        )
    )

    BasicTextField(
        state = state,
        modifier = modifier,
        lineLimits = if(singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.Default,
        interactionSource = interactionSource,
        textStyle = modifiedTextStyle,
        readOnly = readOnly,
        decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if(focused) colors.focusedContainerColor else colors.unfocusedContainerColor,
                        shape = shape
                    )
                    .border(
                        width = 2.dp,
                        color = if(focused) colors.focusedIndicatorColor else colors.unfocusedIndicatorColor,
                        shape = shape
                    )
                    .padding(10.dp)
            ) {
                innerTextField()
            }
        }
    )
}