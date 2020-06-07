package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoEntity(
    @SerializedName("age_group") val ageGroup: Int,
    @SerializedName("description") val description: String,
    @SerializedName("link") val link: String,
    @SerializedName("mentor_id") val mentorId: Int,
    @SerializedName("mentorname") val mentorName: String,
    @SerializedName("status") val status: String,
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("studentname") val studentName: String,
    @SerializedName("thumbnail") var thumbnail: String,
    @SerializedName("title") val title: String,
    @SerializedName("video_id") val videoId: Int
) : Serializable