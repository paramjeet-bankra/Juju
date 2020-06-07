package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoListEntity(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("videos") val videos : ArrayList<VideoEntity>
): Serializable