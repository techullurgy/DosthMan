package com.techullurgy.dosthman.data.di

import androidx.room3.RoomDatabase
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.techullurgy.dosthman.data.room.DosthManDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Module
actual class PlatformDataModule {
    @Singleton
    actual fun provideRoomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<DosthManDatabase> {
        return getDatabaseBuilder()
    }
}

private fun getDatabaseBuilder(): RoomDatabase.Builder<DosthManDatabase> {
    val dbFilePath = documentDirectory() + "/dosthman_db.db"
    return Room.databaseBuilder<DosthManDatabase>(
        name = dbFilePath,
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}