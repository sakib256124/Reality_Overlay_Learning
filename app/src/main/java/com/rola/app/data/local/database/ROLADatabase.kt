package com.rola.app.data.local.database

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rola.app.data.local.dao.ObjectDao
import com.rola.app.data.local.entity.ObjectEntity

@Database(
    entities = [ObjectEntity::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(StringListConverter::class)
abstract class ROLADatabase : RoomDatabase() {
    abstract fun objectDao(): ObjectDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE learning_objects ADD COLUMN facts TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
