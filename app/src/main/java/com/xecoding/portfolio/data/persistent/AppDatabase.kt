package com.xecoding.portfolio.data.persistent

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Account::class, Details::class], version = 1, exportSchema = false
)
@TypeConverters(BeneficiariesConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
    abstract fun detailsDao(): DetailsDao
}