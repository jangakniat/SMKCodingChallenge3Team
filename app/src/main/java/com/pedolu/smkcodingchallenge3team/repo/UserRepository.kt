package com.pedolu.smkcodingchallenge3team.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3team.dao.UserDao
import com.pedolu.smkcodingchallenge3team.data.model.room.UserModel

class UserRepository(private val userDao: UserDao, key: String) {


    val user: LiveData<UserModel> = userDao.getUser(key)

    suspend fun insert(user: UserModel) {
        userDao.insert(user)
    }

    suspend fun update(user: UserModel) {
        userDao.update(user)
    }

}
