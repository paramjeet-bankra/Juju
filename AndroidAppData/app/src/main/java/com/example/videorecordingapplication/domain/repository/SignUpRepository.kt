package com.example.videorecordingapplication.domain.repository

import com.example.videorecordingapplication.data.entity.LoginResponse
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.request.LoginRequest
import com.example.videorecordingapplication.data.entity.request.ModeratorSignUp
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import io.reactivex.Observable

interface SignUpRepository {
    fun registerStudent(studentSignUp: StudentSignUp) : Observable<Response<SignUpResponse>>

    fun registerMentor(moderatorSignUp: ModeratorSignUp): Observable<Response<SignUpResponse>>

    fun login(loginRequest: LoginRequest): Observable<Response<LoginResponse>>


}