package com.pedolu.smkcodingchallenge3.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3.dao.StatusSummaryDao
import com.pedolu.smkcodingchallenge3.data.model.room.StatusSummaryModel

class StatusSummaryRepository(private val statusSummaryDao: StatusSummaryDao, status: String) {
    val statusSummary: LiveData<List<StatusSummaryModel>> =
        statusSummaryDao.getStatusSummary(status)

    suspend fun insertAll(statusSummary: List<StatusSummaryModel>) {
        statusSummaryDao.insertAll(statusSummary)
    }


}