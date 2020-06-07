package com.example.videorecordingapplication.data.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryData(@SerializedName("category_id") val id : Int,
                              @SerializedName("name") val name : String,
                        @SerializedName("imageurl") val imageurl : String) : Serializable