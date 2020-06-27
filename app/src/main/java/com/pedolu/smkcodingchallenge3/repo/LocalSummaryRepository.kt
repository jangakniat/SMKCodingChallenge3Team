package com.pedolu.smkcodingchallenge3.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3.dao.LocalSummaryDao
import com.pedolu.smkcodingchallenge3.data.model.room.LocalSummaryModel

class LocalSummaryRepository(
    private val localSummaryDao: LocalSummaryDao,
    country: String = ""
) {
    val localSummary: LiveData<LocalSummaryModel> = localSummaryDao.getLocalSummary(country)

    suspend fun insert(localSummary: LocalSummaryModel) {
        localSummaryDao.insert(localSummary)
    }

}