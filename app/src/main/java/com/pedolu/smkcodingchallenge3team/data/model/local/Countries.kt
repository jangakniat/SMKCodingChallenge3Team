package com.pedolu.smkcodingchallenge3team.data.model.local


import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("countries")
    val countries: List<Country>
)