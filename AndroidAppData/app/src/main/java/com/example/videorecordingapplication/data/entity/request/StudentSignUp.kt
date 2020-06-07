package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StudentSignUp(
    @SerializedName("age") var age : Int,
    @SerializedName("mentorID") var mentorID : Int,
    @SerializedName("name") var name : String,
    @SerializedName("schoolID") var schoolID : Int,
    @SerializedName("password") var password : String
): Serializable