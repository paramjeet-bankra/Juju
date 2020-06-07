package com.example.videorecordingapplication.data.entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SignUpResponse(@SerializedName("status") val status : String): Serializable
