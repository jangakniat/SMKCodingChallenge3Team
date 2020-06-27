package com.pedolu.smkcodingchallenge3.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge3.data.model.room.IndonesiaSummaryModel
import com.pedolu.smkcodingchallenge3.db.IndonesiaSummaryDatabase
import com.pedolu.smkcodingchallenge3.repo.IndonesiaSummaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndonesiaSummaryViewModel : ViewModel() {

    private lateinit var repository: IndonesiaSummaryRepository
    lateinit var indonesiaSummary: LiveData<List<IndonesiaSummaryModel>>

    fun init(context: Context) {
        val indonesiaSummaryDao =
            IndonesiaSummaryDatabase.getDatabase(context).IndonesiaSummaryDao()
        repository = IndonesiaSummaryRepository(indonesiaSummaryDao)
        indonesiaSummary = repository.indonesiaSummary
    }

    fun addData(indonesiaSummary: List<IndonesiaSummaryModel>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAll(indonesiaSummary)
        }


}
