package com.pedolu.smkcodingchallenge3team.data.model.global


import com.google.gson.annotations.SerializedName
import com.pedolu.smkcodingchallenge3team.data.model.status.Confirmed
import com.pedolu.smkcodingchallenge3team.data.model.status.Deaths
import com.pedolu.smkcodingchallenge3team.data.model.status.Recovered

data class GlobalSummary(
    @SerializedName("confirmed")
    val confirmed: Confirmed,
    @SerializedName("deaths")
    val deaths: Deaths,
    @SerializedName("image")
    val image: String,
    @SerializedName("lastUpdate")
    val lastUpdate: String,
    @SerializedName("recovered")
    val recovered: Recovered

)