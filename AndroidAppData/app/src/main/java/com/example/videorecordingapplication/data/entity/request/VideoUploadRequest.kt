package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoUploadRequest(@SerializedName("categoryID") val categoryID : Int):
    Serializable