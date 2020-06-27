package com.pedolu.smkcodingchallenge3.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountriesModel(
    @PrimaryKey var name: String
) {
    constructor() : this("")
}