package com.pedolu.smkcodingchallenge3team.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(
    var name: String,
    var gender: String,
    var age: String,
    var telp: String,
    var address: String,
    var image:String,
    @PrimaryKey var key: String
) {
    constructor() : this("", "", "", "", "", "","")
}
