package com.example.videorecordingapplication.data.repository

import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.request.ModeratorRequest
import com.example.videorecordingapplication.data.remotedatasource.ApiService
import com.example.videorecordingapplication.domain.repository.ModeratorRepository
import io.reactivex.Observable

class ModeratorRepositoryImpl : ModeratorRepository{
    override fun videoapprove(status: ModeratorRequest): Observable<Response<SignUpResponse>> {
        return ApiService().service.videoApproval(status)
    }
}