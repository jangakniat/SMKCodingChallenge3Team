package com.pedolu.smkcodingchallenge3.data.model.room

import androidx.room.*

@Entity(tableName = "relawan")
data class RelawanModel (
    var name: String,
    var email: String,
    var telp: String,
    var alamat: String,
    @PrimaryKey var key: String
) {
    constructor(): this("", "", "", "", "")
}