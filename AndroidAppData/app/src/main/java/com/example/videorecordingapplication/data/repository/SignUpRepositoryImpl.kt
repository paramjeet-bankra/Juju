package com.example.videorecordingapplication.data.repository

import com.example.videorecordingapplication.data.entity.LoginResponse
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.request.LoginRequest
import com.example.videorecordingapplication.data.entity.request.ModeratorSignUp
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import com.example.videorecordingapplication.data.remotedatasource.ApiService
import com.example.videorecordingapplication.domain.repository.SignUpRepository
import io.reactivex.Observable

class SignUpRepositoryImpl : SignUpRepository {
    override fun registerStudent(studentSignUp: StudentSignUp) : Observable<Response<SignUpResponse>> {
        return ApiService().service.registerStudent(studentSignUp)
    }

    override fun registerMentor(moderatorSignUp: ModeratorSignUp
    ) : Observable<Response<SignUpResponse>> {
        return ApiService().service.registerMentor(moderatorSignUp)
    }

    override fun login(loginRequest: LoginRequest) : Observable<Response<LoginResponse>>{
        return ApiService().service.loginUser(loginRequest)
    }
}