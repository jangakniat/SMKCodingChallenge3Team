package com.pedolu.smkcodingchallenge3.data.model.status


import com.google.gson.annotations.SerializedName

data class Confirmed(
    @SerializedName("detail")
    val detail: String,
    @SerializedName("value")
    val value: Int
)