package com.techullurgy.json_viewer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techullurgy.json_viewer.api.JsonViewerColors
import com.techullurgy.json_viewer.api.JsonViewerConfiguration
import com.techullurgy.json_viewer.api.LocalJsonViewerConfiguration

@Composable
@Preview
private fun JsonViewerPreview() {
    val configuration = JsonViewerConfiguration(
        colors = JsonViewerColors(
            quoteColor = Color.Red,
            colonColor = Color.Blue,
            keyTextColor = Color.Magenta,
            valueNumberColor = Color.Blue,
            valueStringColor = Color.Green,
            valueNullColor = Color.White,
            objectBracketColor = Color.Red,
            arrayBracketColor = Color.Green,
            collapseTriggerColor = Color.LightGray,
            expandTriggerColor = Color.Gray,
            lineNumberColor = Color.LightGray,
            commaColor = Color.Red
        )
    )

    CompositionLocalProvider(
        LocalJsonViewerConfiguration provides configuration
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(40.dp)
        ) {
            JsonViewer(
                json = """
                    [
                        {
                            "name": "Irsath Kareem",
                            "age": 28,
                            "address": [
                                {
                                    "street": "Chinna Pallivasal Street",
                                    "zipCode": "738472"
                                },
                                {
                                    "street": "Puthu Street",
                                    "zipCode": "745263"
                                }
                            ],
                            "phones": {
                                "home": "7643673648",
                                "work": "8934753749"
                            }
                        },
                        {
                            "name": "Irsath Kareem",
                            "age": 28,
                            "address": [
                                {
                                    "street": "Chinna Pallivasal Street",
                                    "zipCode": "738472"
                                },
                                {
                                    "street": "Puthu Street",
                                    "zipCode": "745263"
                                }
                            ],
                            "phones": {
                                "home": "7643673648",
                                "work": "8934753749"
                            }
                        },
                        {
                            "name": "Irsath Kareem",
                            "age": 28,
                            "address": [
                                {
                                    "street": "Chinna Pallivasal Street",
                                    "zipCode": "738472"
                                },
                                {
                                    "street": "Puthu Street",
                                    "zipCode": "745263"
                                }
                            ],
                            "phones": {
                                "home": "7643673648",
                                "work": "8934753749"
                            }
                        },
                        {
                            "name": "Irsath Kareem",
                            "age": 28,
                            "address": [
                                {
                                    "street": "Chinna Pallivasal Street",
                                    "zipCode": "738472"
                                },
                                {
                                    "street": "Puthu Street",
                                    "zipCode": "745263"
                                }
                            ],
                            "phones": {
                                "home": "7643673648",
                                "work": "8934753749"
                            }
                        },
                        {
                            "name": "Irsath Kareem",
                            "age": 28,
                            "address": [
                                {
                                    "street": "Chinna Pallivasal Street",
                                    "zipCode": "738472"
                                },
                                {
                                    "street": "Puthu Street",
                                    "zipCode": "745263"
                                }
                            ],
                            "phones": {
                                "home": "7643673648",
                                "work": "8934753749"
                            }
                        }
                    ]
                """.trimIndent(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}