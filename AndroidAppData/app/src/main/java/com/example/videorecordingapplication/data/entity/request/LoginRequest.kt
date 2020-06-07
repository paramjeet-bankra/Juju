package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginRequest(
    @SerializedName("name") var name : String,
    @SerializedName("type") var type : String,
    @SerializedName("password") var password : String
): Serializable