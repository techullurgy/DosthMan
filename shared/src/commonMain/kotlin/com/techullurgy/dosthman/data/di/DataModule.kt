package com.techullurgy.dosthman.data.di

import androidx.room3.RoomDatabase
import com.techullurgy.dosthman.data.room.DosthManDatabase
import com.techullurgy.dosthman.data.room.RequestResponseDao
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module(includes = [PlatformDataModule::class])
@ComponentScan("com.techullurgy.dosthman.data")
class DataModule {
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json()
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    @Singleton
    fun provideDosthManDatabase(
        builder: RoomDatabase.Builder<DosthManDatabase>
    ): DosthManDatabase {
        return builder.build()
    }

    @Singleton
    fun provideRequestResponseDao(
        database: DosthManDatabase
    ): RequestResponseDao {
        return database.requestResponseDao()
    }

    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}