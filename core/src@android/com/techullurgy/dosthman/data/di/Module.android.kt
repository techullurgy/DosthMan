package com.techullurgy.dosthman.data.di

import androidx.room3.RoomDatabase
import com.techullurgy.dosthman.data.room.AppDatabase
import com.techullurgy.dosthman.data.room.getDatabaseBuilder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@ComponentScan("com.techullurgy.dosthman.data")
actual class PlatformDataModule

actual val platformDataModule: org.koin.core.module.Module = module { 
    single<RoomDatabase.Builder<AppDatabase>> { getDatabaseBuilder(get()) }
}