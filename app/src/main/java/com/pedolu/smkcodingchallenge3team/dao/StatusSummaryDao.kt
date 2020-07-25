package com.pedolu.smkcodingchallenge3team.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedolu.smkcodingchallenge3team.data.model.room.StatusSummaryModel

@Dao
interface StatusSummaryDao {
    @Query("SELECT * from status_summary WHERE status=:status")
    fun getStatusSummary(status: String): LiveData<List<StatusSummaryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(statusSummary: List<StatusSummaryModel>)
}