package com.pedolu.smkcodingchallenge3team.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedolu.smkcodingchallenge3team.dao.VolunteerDao
import com.pedolu.smkcodingchallenge3team.data.model.room.VolunteerModel

@Database(entities = arrayOf(VolunteerModel::class), version = 1, exportSchema = false)
abstract class VolunteerDatabase : RoomDatabase() {

    abstract fun VolunteerDao(): VolunteerDao

    companion object {
        @Volatile
        private var INSTANCE: VolunteerDatabase? = null

        fun getDatabase(context: Context): VolunteerDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VolunteerDatabase::class.java,
                    "volunteers"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
