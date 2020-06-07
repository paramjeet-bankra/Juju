package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModeratorRequest(
    @SerializedName("status") val status : String,
    @SerializedName("videoID") val videoID: Int): Serializable