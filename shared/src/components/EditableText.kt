package com.techullurgy.dosthman.shared.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.techullurgy.dosthman.shared.modifiers.preserveFocusOnTapBounds
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@Composable
internal fun EditableText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    var isInEditMode by retain { mutableStateOf(false) }
    var capturedText by retain { mutableStateOf("") }
    if (isInEditMode) {
        EditableTextField(
            initialText = capturedText,
            onTextChange = onTextChange,
            onDismiss = { isInEditMode = false },
            singleLine = singleLine,
            textStyle = textStyle,
            modifier = modifier,
        )
    } else {
        Text(
            text = text,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            overflow = overflow,
            style = textStyle,
            modifier = modifier.combinedClickable(
                onClick = {},
                onDoubleClick = {
                    capturedText = text
                    isInEditMode = true
                },
            ),
        )
    }
}

@Composable
private fun EditableTextField(
    initialText: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    singleLine: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val textState = rememberTextFieldState(initialText = initialText)
    val focusRequester = remember { FocusRequester() }
    val currentOnTextChange by rememberUpdatedState(onTextChange)
    var hasBeenFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(textState) {
        snapshotFlow { textState.text.toString() }
            .drop(1)
            .distinctUntilChanged()
            .collect { currentOnTextChange(it) }
    }

    val resolvedTextStyle = textStyle.copy(
        color = textStyle.color.takeOrElse { LocalContentColor.current },
    )

    BasicTextField(
        state = textState,
        textStyle = resolvedTextStyle,
        lineLimits = if (singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.Default,
        keyboardOptions = if (singleLine) KeyboardOptions(imeAction = ImeAction.Done) else KeyboardOptions.Default,
        onKeyboardAction = if (singleLine) { _ -> onDismiss() } else null,
        modifier = modifier
            .preserveFocusOnTapBounds()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    hasBeenFocused = true
                } else if (hasBeenFocused) {
                    onDismiss()
                }
            },
    )
}