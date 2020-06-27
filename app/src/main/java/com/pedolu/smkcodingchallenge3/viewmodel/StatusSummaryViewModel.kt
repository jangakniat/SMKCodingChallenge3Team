package com.pedolu.smkcodingchallenge3.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge3.data.model.room.StatusSummaryModel
import com.pedolu.smkcodingchallenge3.db.StatusSummaryDatabase
import com.pedolu.smkcodingchallenge3.repo.StatusSummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatusSummaryViewModel : ViewModel() {

    private lateinit var repository: StatusSummaryRepository
    lateinit var statusSummary: LiveData<List<StatusSummaryModel>>

    fun init(context: Context, status: String) {
        val statusSummaryDao =
            StatusSummaryDatabase.getDatabase(context).StatusSummaryDao()
        repository = StatusSummaryRepository(statusSummaryDao, status)
        statusSummary = repository.statusSummary
    }

    fun addAllData(statusSummary: List<StatusSummaryModel>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAll(statusSummary)
        }


}