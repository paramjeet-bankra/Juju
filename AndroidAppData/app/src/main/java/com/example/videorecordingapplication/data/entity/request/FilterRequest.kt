package com.example.videorecordingapplication.data.entity.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FilterRequest(@SerializedName("categories") val categories : ArrayList<Int>): Serializable