package com.pedolu.smkcodingchallenge3team.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_summary")
data class CountrySummaryModel(
    var confirmed: String,
    var recovered: String,
    var deaths: String,
    var last_update: String,
    @PrimaryKey
    var country_name: String
) {
    constructor() : this("", "", "", "", "")
}
