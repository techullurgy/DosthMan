package com.techullurgy.dosthman.data.room

import androidx.room3.Room
import androidx.room3.RoomDatabase
import org.koin.core.annotation.Singleton
import java.io.File

@Singleton
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
//    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    val dbFile = File( "temp", "my_room.db").also { it.parentFile?.mkdirs() }
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}