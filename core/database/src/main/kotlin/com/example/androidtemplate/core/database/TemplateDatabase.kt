package com.example.androidtemplate.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CatalogEntity::class], version = 1, exportSchema = true)
abstract class TemplateDatabase : RoomDatabase() {
    abstract fun catalogDao(): CatalogDao
}
