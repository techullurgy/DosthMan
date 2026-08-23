@file:OptIn(ExperimentalObjCRefinement::class)

package com.techullurgy.dosthman

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.techullurgy.dosthman.data.di.DataModule
import org.koin.compose.KoinApplication as DosthmanKoinApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.includes
import org.koin.dsl.koinConfiguration
import org.koin.plugin.module.dsl.koinConfiguration
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

@HiddenFromObjC
@KoinApplication
@ComponentScan("com.techullurgy.dosthman")
internal class DosthmanApp

@Configuration
@Module(includes = [DataModule::class])
internal class AppModule

@Composable
@Preview
fun App() {
    DosthmanKoinApplication(
        configuration = koinConfiguration {
            val appConfiguration = koinConfiguration<DosthmanApp>()
            includes(appConfiguration)
        }
    ) {

    }
}