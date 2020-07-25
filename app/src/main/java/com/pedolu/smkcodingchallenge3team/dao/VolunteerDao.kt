package com.pedolu.smkcodingchallenge3team.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedolu.smkcodingchallenge3team.data.model.room.VolunteerModel

@Dao
interface VolunteerDao {
    @Query("SELECT * from volunteers where `key`!=:key")
    fun getAllVolunteer(key: String): LiveData<List<VolunteerModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(user: List<VolunteerModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: VolunteerModel)
}