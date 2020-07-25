package com.pedolu.smkcodingchallenge3team.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge3team.data.model.room.CountrySummaryModel
import com.pedolu.smkcodingchallenge3team.db.CountrySummaryDatabase
import com.pedolu.smkcodingchallenge3team.repo.CountrySummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountrySummaryViewModel : ViewModel() {

    private lateinit var repository: CountrySummaryRepository
    lateinit var countrySummary: LiveData<List<CountrySummaryModel>>

    fun init(context: Context) {
        val countrySummaryDao = CountrySummaryDatabase.getDatabase(context).CountrySummaryDao()
        repository = CountrySummaryRepository(countrySummaryDao)
        countrySummary = repository.countrySummary
    }

    fun addAllData(countrySummary: List<CountrySummaryModel>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAll(countrySummary)
        }

}