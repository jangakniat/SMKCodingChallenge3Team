package com.pedolu.smkcodingchallenge3team.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge3team.dao.CountrySummaryDao
import com.pedolu.smkcodingchallenge3team.data.model.room.CountrySummaryModel

@Database(entities = [CountrySummaryModel::class], version = 1, exportSchema = false)
abstract class CountrySummaryDatabase : RoomDatabase() {

    abstract fun CountrySummaryDao(): CountrySummaryDao

    companion object {
        @Volatile
        private var INSTANCE: CountrySummaryDatabase? = null

        fun getDatabase(context: Context): CountrySummaryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountrySummaryDatabase::class.java,
                    "country_summary"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
