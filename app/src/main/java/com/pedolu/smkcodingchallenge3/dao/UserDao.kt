package com.pedolu.smkcodingchallenge3.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedolu.smkcodingchallenge3.data.model.room.UserModel

@Dao
interface UserDao {
    @Query("SELECT * from users where `key`=:key")
    fun getUser(key: String): LiveData<UserModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserModel)
}