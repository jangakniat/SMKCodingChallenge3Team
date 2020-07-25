package com.pedolu.smkcodingchallenge3team.repo

import androidx.lifecycle.LiveData
import com.pedolu.smkcodingchallenge3team.dao.IndonesiaSummaryDao
import com.pedolu.smkcodingchallenge3team.data.model.room.IndonesiaSummaryModel

class IndonesiaSummaryRepository(private val indonesiaSummaryDao: IndonesiaSummaryDao) {
    val indonesiaSummary: LiveData<List<IndonesiaSummaryModel>> =
        indonesiaSummaryDao.getIndonesiaSummary()

    suspend fun insertAll(indonesiaSummary: List<IndonesiaSummaryModel>) {
        indonesiaSummaryDao.insertAll(indonesiaSummary)
    }

}