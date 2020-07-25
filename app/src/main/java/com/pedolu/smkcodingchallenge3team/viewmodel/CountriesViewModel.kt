package com.pedolu.smkcodingchallenge3team.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedolu.smkcodingchallenge3team.data.model.room.CountriesModel
import com.pedolu.smkcodingchallenge3team.db.CountriesDatabase
import com.pedolu.smkcodingchallenge3team.repo.CountriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {

    private lateinit var repository: CountriesRepository
    lateinit var countries: LiveData<List<CountriesModel>>

    fun init(context: Context) {
        val countriesDao = CountriesDatabase.getDatabase(context).CountriesDao()
        repository = CountriesRepository(countriesDao)
        countries = repository.countries
    }


    fun addAllData(countries: List<CountriesModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        repository.insertAll(countries)
    }


}