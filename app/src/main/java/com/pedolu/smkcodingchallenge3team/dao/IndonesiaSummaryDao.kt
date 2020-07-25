package com.pedolu.smkcodingchallenge3team.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedolu.smkcodingchallenge3team.data.model.room.IndonesiaSummaryModel


@Dao
interface IndonesiaSummaryDao {
    @Query("SELECT * from indonesia_summary")
    fun getIndonesiaSummary(): LiveData<List<IndonesiaSummaryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(indonesia: List<IndonesiaSummaryModel>)
}