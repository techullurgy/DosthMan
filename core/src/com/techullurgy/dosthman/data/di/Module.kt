package com.techullurgy.dosthman.data.di

import com.techullurgy.dosthman.data.KtorNetworkCaller
import com.techullurgy.dosthman.data.room.AppDatabase
import com.techullurgy.dosthman.data.room.HttpRequestDao
import com.techullurgy.dosthman.data.room.RoomHttpRequestPersistentStorage
import com.techullurgy.dosthman.data.room.getRoomDatabase
import com.techullurgy.dosthman.domain.HttpRequestPersistentStorage
import com.techullurgy.dosthman.domain.NetworkCaller
import com.techullurgy.dosthman.domain.HttpRequestResponseManager
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.dsl.module

@Module
expect class PlatformDataModule

@Module(includes = [PlatformDataModule::class])
@ComponentScan("com.techullurgy.basecmpproject.data")
class DataModule {
    @Singleton
    fun httpRequestDao(db: AppDatabase): HttpRequestDao = db.httpRequestDao()
}


expect val platformDataModule: org.koin.core.module.Module

val dataModule = module {
    single<HttpRequestDao> { get<AppDatabase>().httpRequestDao() }
    single<NetworkCaller> { KtorNetworkCaller() }
    single<AppDatabase> { getRoomDatabase(get()) }
    single<HttpRequestPersistentStorage> { RoomHttpRequestPersistentStorage(get()) }
    single<HttpRequestResponseManager> { HttpRequestResponseManager(get()) }

    includes(platformDataModule)
}