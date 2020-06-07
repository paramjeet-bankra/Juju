package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SchoolEntity(
    @SerializedName("domain") val domain: String,
    @SerializedName("name") val name: String,
    @SerializedName("school_id") val school_id: Int
): Serializable

