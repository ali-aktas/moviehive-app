package com.aliaktas.cinematch.data.model

import android.provider.MediaStore
import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("results") val results: List<Video>
)