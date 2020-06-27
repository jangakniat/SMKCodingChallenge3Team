package com.pedolu.smkcodingchallenge3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge3.dao.StatusSummaryDao
import com.pedolu.smkcodingchallenge3.data.model.room.StatusSummaryModel

@Database(entities = [StatusSummaryModel::class], version = 1, exportSchema = false)
abstract class StatusSummaryDatabase : RoomDatabase() {

    abstract fun StatusSummaryDao(): StatusSummaryDao

    companion object {
        @Volatile
        private var INSTANCE: StatusSummaryDatabase? = null

        fun getDatabase(context: Context): StatusSummaryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatusSummaryDatabase::class.java,
                    "status_summary"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
