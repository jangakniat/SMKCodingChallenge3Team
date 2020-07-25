package com.pedolu.smkcodingchallenge3team.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge3team.data.model.room.VolunteerModel
import com.pedolu.smkcodingchallenge3team.db.VolunteerDatabase
import com.pedolu.smkcodingchallenge3team.repo.VolunteerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VolunteerViewModel : ViewModel() {

    private lateinit var repository: VolunteerRepository
    lateinit var volunteers: LiveData<List<VolunteerModel>>

    fun init(context: Context, key: String) {
        val volunteerDao = VolunteerDatabase.getDatabase(context).VolunteerDao()
        repository = VolunteerRepository(volunteerDao, key)
        volunteers = repository.volunteers
    }

    fun addAllData(volunteers: List<VolunteerModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(volunteers)
    }


    fun updateData(volunteer: VolunteerModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(volunteer)
    }

}
