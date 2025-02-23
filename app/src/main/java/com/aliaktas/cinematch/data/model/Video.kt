package com.aliaktas.cinematch.data.model

import com.google.gson.annotations.SerializedName

class Video (
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String
)