package com.pedolu.smkcodingchallenge3team.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge3team.dao.IndonesiaSummaryDao
import com.pedolu.smkcodingchallenge3team.data.model.room.IndonesiaSummaryModel

@Database(entities = [IndonesiaSummaryModel::class], version = 1, exportSchema = false)
abstract class IndonesiaSummaryDatabase : RoomDatabase() {

    abstract fun IndonesiaSummaryDao(): IndonesiaSummaryDao

    companion object {
        @Volatile
        private var INSTANCE: IndonesiaSummaryDatabase? = null

        fun getDatabase(context: Context): IndonesiaSummaryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IndonesiaSummaryDatabase::class.java,
                    "indonesia_summary"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
