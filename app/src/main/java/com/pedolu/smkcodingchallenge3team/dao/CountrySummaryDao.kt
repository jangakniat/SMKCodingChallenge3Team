package com.pedolu.smkcodingchallenge3team.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedolu.smkcodingchallenge3team.data.model.room.CountrySummaryModel

@Dao
interface CountrySummaryDao {

    @Query("SELECT * from country_summary")
    fun getAllCountrySummary(): LiveData<List<CountrySummaryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countrySummary: List<CountrySummaryModel>)

}