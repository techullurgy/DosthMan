package com.techullurgy.dosthman.data.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.dosthman.data.room.DosthManDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope
import java.io.File

@Module
actual class PlatformDataModule {
    @Singleton
    actual fun provideRoomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<DosthManDatabase> {
        return getDatabaseBuilder()
    }
}

private fun getDatabaseBuilder(): RoomDatabase.Builder<DosthManDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "dosthman_db.db")
    return Room.databaseBuilder<DosthManDatabase>(
        name = dbFile.absolutePath,
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}