package com.pedolu.smkcodingchallenge3team.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pedolu.smkcodingchallenge3team.data.model.room.CountriesModel

@Dao
interface CountriesDao {
    @Query("SELECT * from countries")
    fun getCountries(): LiveData<List<CountriesModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountriesModel>)

    @Query("DELETE FROM countries")
    suspend fun deleteAll()

}