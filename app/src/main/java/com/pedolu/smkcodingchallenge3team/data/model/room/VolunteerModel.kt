package com.pedolu.smkcodingchallenge3team.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volunteers")
data class VolunteerModel(
    var name: String,
    var telp: String,
    var address: String,
    var image: String,
    @PrimaryKey var key: String
) {
    constructor() : this("", "", "", "", "")
}