package com.pedolu.smkcodingchallenge3team.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3team.dao.CountrySummaryDao
import com.pedolu.smkcodingchallenge3team.data.model.room.CountrySummaryModel

class CountrySummaryRepository(
    private val countrySummaryDao: CountrySummaryDao
) {
    val countrySummary: LiveData<List<CountrySummaryModel>> =
        countrySummaryDao.getAllCountrySummary()

    suspend fun insertAll(countrySummary: List<CountrySummaryModel>) {
        countrySummaryDao.insertAll(countrySummary)
    }

}