package com.pedolu.smkcodingchallenge3.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3.dao.IndonesiaSummaryDao
import com.pedolu.smkcodingchallenge3.data.model.room.IndonesiaSummaryModel

class IndonesiaSummaryRepository(private val indonesiaSummaryDao: IndonesiaSummaryDao) {
    val indonesiaSummary: LiveData<List<IndonesiaSummaryModel>> =
        indonesiaSummaryDao.getIndonesiaSummary()

    suspend fun insertAll(indonesiaSummary: List<IndonesiaSummaryModel>) {
        indonesiaSummaryDao.insertAll(indonesiaSummary)
    }

}