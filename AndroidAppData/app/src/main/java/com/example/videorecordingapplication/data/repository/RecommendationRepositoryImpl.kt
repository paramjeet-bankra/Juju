package com.example.videorecordingapplication.data.repository

import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.remotedatasource.ApiService
import com.example.videorecordingapplication.domain.repository.RecommendationRepository
import io.reactivex.Observable

class RecommendationRepositoryImpl : RecommendationRepository {
    override fun getRecommendations() : Observable<Response<ArrayList<CategoryData>>>{
        return ApiService().service.getCategories()
    }
}