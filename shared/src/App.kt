package com.techullurgy.dosthman.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.techullurgy.dosthman.shared.di.appModule
import com.techullurgy.dosthman.shared.screens.workspace.ApiWorkspaceScreen
import com.techullurgy.dosthman.shared.theme.DosthManTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    DosthManTheme {
        KoinApplication(
            configuration = koinConfiguration {
                modules(appModule)
            }
        ) {
            val backStack = rememberNavBackStack(
                configuration = configuration,
                ApiWorkspace
            )

            TestDisplay(
                backStack = backStack,
                entryProvider = entryProvider {
                    entry<ApiWorkspace> {
                        ApiWorkspaceScreen(Modifier.fillMaxSize())
                    }
                }
            )
        }
    }
}

@Composable
private fun TestDisplay(
    backStack: NavBackStack<NavKey>,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider,
        modifier = modifier,
    )
}

private val configuration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ApiWorkspace::class, ApiWorkspace.serializer())
        }
    }
}

@Serializable
data object ApiWorkspace: NavKey
