package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse(
    @SerializedName("age") val age : Int,
    @SerializedName("mentor_id") val mentorId : Int,
    @SerializedName("mentorname") val mentorName : String,
    @SerializedName("name") val name : String,
    @SerializedName("school_id") val schoolId : Int,
    @SerializedName("schoolname") val schoolName : String,
    @SerializedName("student_id") val studentId : Int
): Serializable
