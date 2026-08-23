package com.techullurgy.dosthman.data.di

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.techullurgy.dosthman.data.room.DosthManDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
actual class PlatformDataModule {
    @Singleton
    actual fun provideRoomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<DosthManDatabase> {
        return getDatabaseBuilder(scope.get<Context>())
    }
}

private fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<DosthManDatabase> {
    val appContext: Context = context.applicationContext
    val dbFile = appContext.getDatabasePath("dosthman_db.db")
    return Room.databaseBuilder<DosthManDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .setQueryCoroutineContext(Dispatchers.IO)
}