package com.pedolu.smkcodingchallenge3team.data.model.global


import com.google.gson.annotations.SerializedName

data class GlobalStatusSummaryItem(
    @SerializedName("active")
    val active: Int,
    @SerializedName("combinedKey")
    val combinedKey: String,
    @SerializedName("confirmed")
    val confirmed: Int,
    @SerializedName("countryRegion")
    val countryRegion: String,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("recovered")
    val recovered: Int

)