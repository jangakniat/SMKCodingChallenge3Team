package com.pedolu.smkcodingchallenge3.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status_summary")
data class StatusSummaryModel(
    var count: Int,
    var status: String,
    @PrimaryKey
    var combined_key: String
) {
    constructor() : this(0, "", "")
}