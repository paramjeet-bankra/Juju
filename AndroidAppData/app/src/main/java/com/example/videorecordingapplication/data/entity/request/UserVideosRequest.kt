package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserVideosRequest(
    @SerializedName("type") val type : String,
    @SerializedName("userID") val userID: Int) : Serializable