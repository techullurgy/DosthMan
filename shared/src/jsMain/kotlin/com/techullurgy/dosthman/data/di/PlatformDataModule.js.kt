package com.techullurgy.dosthman.data.di

import androidx.room3.RoomDatabase
import com.techullurgy.dosthman.data.room.DosthManDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Singleton
import org.koin.core.scope.Scope

@Module
actual class PlatformDataModule {
    @Singleton
    actual fun provideRoomDatabaseBuilder(@Provided scope: Scope): RoomDatabase.Builder<DosthManDatabase> {
        TODO("Not yet implemented")
    }
}