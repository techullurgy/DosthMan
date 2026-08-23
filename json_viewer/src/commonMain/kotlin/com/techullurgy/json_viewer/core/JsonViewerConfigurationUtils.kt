package com.techullurgy.json_viewer.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.techullurgy.json_viewer.api.JsonViewerColors
import com.techullurgy.json_viewer.api.JsonViewerConfiguration
import com.techullurgy.json_viewer.api.JsonViewerStyle

private const val COLLAPSE_TRIGGER_SIZE_DP = 8

@Composable
internal fun mergeWithDefaultJsonViewerConfiguration(
    configuration: JsonViewerConfiguration
): JsonViewerConfiguration {
    val defaultConfiguration = getDefaultJsonViewerConfiguration()
    return configuration.mergeWith(defaultConfiguration)
}

@Composable
private fun getDefaultJsonViewerConfiguration(): JsonViewerConfiguration {
    val isDarkTheme = isSystemInDarkTheme()

    val containerColor = if (isDarkTheme) Color.Black else Color.White
    val contentColor = if (isDarkTheme) Color.White else Color.Black

    val jsonTextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
    val lineNoTextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
    val collapseExpandTriggerSizeDp = COLLAPSE_TRIGGER_SIZE_DP

    val defaultConfiguration = JsonViewerConfiguration(
        style = setJsonViewerStylesAndGet(
            jsonStyle = jsonTextStyle,
            lineNoStyle = lineNoTextStyle,
            collapseExpandTriggerSizeDp = collapseExpandTriggerSizeDp
        ),
        colors = setJsonViewerColorsAndGet(
            containerColor = containerColor,
            contentColor = contentColor
        )
    )

    return defaultConfiguration
}

private fun setJsonViewerStylesAndGet(
    jsonStyle: TextStyle,
    lineNoStyle: TextStyle,
    @Suppress("SameParameterValue")
    collapseExpandTriggerSizeDp: Int
): JsonViewerStyle {
    return JsonViewerStyle(
        fontSize = jsonStyle.fontSize,
        jsonFontWeight = jsonStyle.fontWeight,
        lineNoFontWeight = lineNoStyle.fontWeight,
        collapseExpandTriggerSizeDp = collapseExpandTriggerSizeDp
    )
}

private fun setJsonViewerColorsAndGet(
    containerColor: Color,
    contentColor: Color
): JsonViewerColors {
    return JsonViewerColors(
        quoteColor = contentColor,
        colonColor = contentColor,
        keyTextColor = contentColor,
        valueNumberColor = contentColor,
        valueStringColor = contentColor,
        valueNullColor = contentColor,
        objectBracketColor = contentColor,
        arrayBracketColor = contentColor,
        collapseTriggerColor = contentColor,
        expandTriggerColor = contentColor,
        lineNumberColor = contentColor,
        commaColor = contentColor,
        gutterContainerColor = containerColor,
        jsonContainerColor = containerColor
    )
}

private fun JsonViewerConfiguration.mergeWith(other: JsonViewerConfiguration): JsonViewerConfiguration {
    return JsonViewerConfiguration(
        colors = JsonViewerColors(
            quoteColor = if(!colors.quoteColor.isUnspecified) colors.quoteColor else other.colors.quoteColor,
            colonColor = if(!colors.colonColor.isUnspecified) colors.colonColor else other.colors.colonColor,
            keyTextColor = if(!colors.keyTextColor.isUnspecified) colors.keyTextColor else other.colors.keyTextColor,
            valueNumberColor = if(!colors.valueNumberColor.isUnspecified) colors.valueNumberColor else other.colors.valueNumberColor,
            valueStringColor = if(!colors.valueStringColor.isUnspecified) colors.valueStringColor else other.colors.valueStringColor,
            valueNullColor = if(!colors.valueNullColor.isUnspecified) colors.valueNullColor else other.colors.valueNullColor,
            objectBracketColor = if(!colors.objectBracketColor.isUnspecified) colors.objectBracketColor else other.colors.objectBracketColor,
            arrayBracketColor = if(!colors.arrayBracketColor.isUnspecified) colors.arrayBracketColor else other.colors.arrayBracketColor,
            collapseTriggerColor = if(!colors.collapseTriggerColor.isUnspecified) colors.collapseTriggerColor else other.colors.collapseTriggerColor,
            expandTriggerColor = if(!colors.expandTriggerColor.isUnspecified) colors.expandTriggerColor else other.colors.expandTriggerColor,
            lineNumberColor = if(!colors.lineNumberColor.isUnspecified) colors.lineNumberColor else other.colors.lineNumberColor,
            commaColor = if(!colors.commaColor.isUnspecified) colors.commaColor else other.colors.commaColor,
            gutterContainerColor = if(!colors.gutterContainerColor.isUnspecified) colors.gutterContainerColor else other.colors.gutterContainerColor,
            jsonContainerColor = if(!colors.jsonContainerColor.isUnspecified) colors.jsonContainerColor else other.colors.jsonContainerColor,
        ),
        style = JsonViewerStyle(
            fontSize = if(!style.fontSize.isUnspecified) style.fontSize else other.style.fontSize,
            jsonFontWeight = style.jsonFontWeight ?: other.style.jsonFontWeight,
            lineNoFontWeight = style.lineNoFontWeight ?: other.style.lineNoFontWeight,
            collapseExpandTriggerSizeDp = if(style.collapseExpandTriggerSizeDp != Int.MIN_VALUE) style.collapseExpandTriggerSizeDp else other.style.collapseExpandTriggerSizeDp
        )
    )
}