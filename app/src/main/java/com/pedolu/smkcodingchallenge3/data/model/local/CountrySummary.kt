package com.pedolu.smkcodingchallenge3.data.model.local


import com.google.gson.annotations.SerializedName
import com.pedolu.smkcodingchallenge3.data.model.status.Confirmed
import com.pedolu.smkcodingchallenge3.data.model.status.Deaths
import com.pedolu.smkcodingchallenge3.data.model.status.Recovered

data class CountrySummary(
    @SerializedName("confirmed")
    val confirmed: Confirmed,
    @SerializedName("deaths")
    val deaths: Deaths,
    @SerializedName("lastUpdate")
    val lastUpdate: String,
    @SerializedName("recovered")
    val recovered: Recovered
)