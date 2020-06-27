package com.pedolu.smkcodingchallenge3.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indonesia_summary")
data class IndonesiaSummaryModel(
    var terkonfirmasi: String,
    var sembuh: String,
    var meninggal: String,
    @PrimaryKey var nama_provinsi: String
) {
    constructor() : this("", "", "", "")
}