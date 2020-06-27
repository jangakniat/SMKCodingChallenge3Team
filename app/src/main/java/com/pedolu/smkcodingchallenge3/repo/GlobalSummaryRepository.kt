package com.pedolu.smkcodingchallenge3.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3.dao.GlobalSummaryDao
import com.pedolu.smkcodingchallenge3.data.model.room.GlobalSummaryModel

class GlobalSummaryRepository(private val globalSummaryDao: GlobalSummaryDao) {
    val globalSummary: LiveData<GlobalSummaryModel> = globalSummaryDao.getGlobalSummary()

    suspend fun insert(globalSummary: GlobalSummaryModel) {
        globalSummaryDao.insert(globalSummary)
    }

}