package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UploadResponse(@SerializedName("ETag") val etag : String,
                          @SerializedName("objURL") val objURL : String): Serializable

