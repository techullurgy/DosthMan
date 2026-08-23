package com.techullurgy.dosthman.data.room

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@Database(
    entities = [RequestResponseEntity::class],
    version = 1
)
@ColumnTypeConverters(PersistentRequestResponseColumnTypeConverter::class)
@ConstructedBy(DosthManDatabaseConstructor::class)
abstract class DosthManDatabase: RoomDatabase() {
    abstract fun requestResponseDao(): RequestResponseDao
}

@Suppress("KotlinNoActualForExpect")
expect object DosthManDatabaseConstructor : RoomDatabaseConstructor<DosthManDatabase> {
    override fun initialize(): DosthManDatabase
}