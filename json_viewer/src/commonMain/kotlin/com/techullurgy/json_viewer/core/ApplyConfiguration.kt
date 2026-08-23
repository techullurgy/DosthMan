package com.techullurgy.json_viewer.core

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.techullurgy.json_viewer.api.JsonViewerColors

fun AnnotatedString.applyColors(
    colors: JsonViewerColors
): AnnotatedString {
    val quoteRanges = getStringAnnotations(QUOTE_TEXT, 0, length)
    val colonRanges = getStringAnnotations(COLON_TEXT, 0, length)
    val keyRanges = getStringAnnotations(KEY_TEXT, 0, length)
    val valueNumberRanges = getStringAnnotations(VALUE_NUMBER_TEXT, 0, length)
    val valueStringRanges = getStringAnnotations(VALUE_STRING_TEXT, 0, length)
    val valueNullRanges = getStringAnnotations(VALUE_NULL_TEXT, 0, length)
    val objectBracketRanges = getStringAnnotations(OBJECT_BRACKET_TEXT, 0, length)
    val arrayBracketRanges = getStringAnnotations(ARRAY_BRACKET_TEXT, 0, length)
    val commaRanges = getStringAnnotations(COMMA_TEXT, 0, length)

    return buildAnnotatedString {
        append(this@applyColors)

        quoteRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.quoteColor),
                start = it.start,
                end = it.end
            )
        }

        colonRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.colonColor),
                start = it.start,
                end = it.end
            )
        }

        keyRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.keyTextColor),
                start = it.start,
                end = it.end
            )
        }

        valueNumberRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.valueNumberColor),
                start = it.start,
                end = it.end
            )
        }

        valueStringRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.valueStringColor),
                start = it.start,
                end = it.end
            )
        }

        valueNullRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.valueNullColor),
                start = it.start,
                end = it.end
            )
        }

        objectBracketRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.objectBracketColor),
                start = it.start,
                end = it.end
            )
        }

        arrayBracketRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.arrayBracketColor),
                start = it.start,
                end = it.end
            )
        }

        commaRanges.forEach {
            addStyle(
                style = SpanStyle(color = colors.commaColor),
                start = it.start,
                end = it.end
            )
        }
    }
}