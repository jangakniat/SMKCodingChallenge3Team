package com.pedolu.smkcodingchallenge3team.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge3team.data.model.room.GlobalSummaryModel
import com.pedolu.smkcodingchallenge3team.db.GlobalSummaryDatabase
import com.pedolu.smkcodingchallenge3team.repo.GlobalSummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalSummaryViewModel : ViewModel() {

    private lateinit var repository: GlobalSummaryRepository
    lateinit var globalSummary: LiveData<GlobalSummaryModel>

    fun init(context: Context) {
        val globalSummaryDao = GlobalSummaryDatabase.getDatabase(context).GlobalSummaryDao()
        repository = GlobalSummaryRepository(globalSummaryDao)
        globalSummary = repository.globalSummary
    }

    fun addData(globalSummary: GlobalSummaryModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(globalSummary)
    }


}
