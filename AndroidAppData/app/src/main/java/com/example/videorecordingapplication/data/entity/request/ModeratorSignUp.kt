package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModeratorSignUp(
    @SerializedName("emailID") var emailID : String,
    @SerializedName("name") var name : String,
    @SerializedName("schoolID") var schoolID : Int,
    @SerializedName("password") var password : String
): Serializable