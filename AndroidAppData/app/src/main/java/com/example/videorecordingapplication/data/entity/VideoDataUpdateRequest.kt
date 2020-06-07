package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoDataUpdateRequest(
    @SerializedName("title") val title : String,
    @SerializedName("desc") val desc : String,
    @SerializedName("videoURL") val videoURL : String,
    @SerializedName("thumnailURL") val thumnailURL : String,
    @SerializedName("studentID") val studentID : Int,
    @SerializedName("categoryID") val categoryID : Int,
    @SerializedName("mentorID") val mentorID : Int,
    @SerializedName("ageGroup") val ageGroup : Int
) : Serializable