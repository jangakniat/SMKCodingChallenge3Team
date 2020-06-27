package com.pedolu.smkcodingchallenge3.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedolu.smkcodingchallenge3.data.model.room.GlobalSummaryModel

@Dao
interface GlobalSummaryDao {
    @Query("SELECT * from global_summary")
    fun getGlobalSummary(): LiveData<GlobalSummaryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: GlobalSummaryModel)

}