package com.example.videorecordingapplication.domain.repository

import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.Response
import io.reactivex.Observable


interface RecommendationRepository {
    fun getRecommendations() : Observable<Response<ArrayList<CategoryData>>>
}