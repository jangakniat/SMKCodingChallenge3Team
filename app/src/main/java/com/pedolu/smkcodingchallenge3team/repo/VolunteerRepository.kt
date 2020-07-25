package com.pedolu.smkcodingchallenge3team.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3team.dao.VolunteerDao
import com.pedolu.smkcodingchallenge3team.data.model.room.VolunteerModel

class VolunteerRepository(private val volunteerDao: VolunteerDao, key: String) {


    val volunteers: LiveData<List<VolunteerModel>> = volunteerDao.getAllVolunteer(key)

    suspend fun insertAll(volunteers: List<VolunteerModel>) {
        volunteerDao.insertAll(volunteers)
    }

    suspend fun update(volunteer: VolunteerModel) {
        volunteerDao.update(volunteer)
    }

}
