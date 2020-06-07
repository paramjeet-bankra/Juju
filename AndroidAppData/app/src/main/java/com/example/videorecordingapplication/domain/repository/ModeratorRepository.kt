package com.example.videorecordingapplication.domain.repository

import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.request.ModeratorRequest
import io.reactivex.Observable

interface ModeratorRepository {
    fun videoapprove(status : ModeratorRequest) : Observable<Response<SignUpResponse>>
}