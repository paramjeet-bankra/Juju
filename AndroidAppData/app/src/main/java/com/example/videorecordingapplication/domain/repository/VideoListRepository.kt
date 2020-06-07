package com.example.videorecordingapplication.domain.repository

import com.example.videorecordingapplication.data.entity.*
import com.example.videorecordingapplication.data.entity.request.FilterRequest
import com.example.videorecordingapplication.data.entity.request.UserVideosRequest
import com.example.videorecordingapplication.data.entity.request.VideoUploadRequest
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface VideoListRepository {
    fun getUserVideos(userVideosRequest: UserVideosRequest) : Observable<Response<ArrayList<VideoEntity>>>
    fun getFilteredVideos(filterRequest: FilterRequest) : Observable<Response<ArrayList<VideoListEntity>>>
    fun getAllVideos() : Observable<Response<ArrayList<VideoListEntity>>>
    fun uploadData(key:String,desc: RequestBody, multipart : MultipartBody.Part) : Observable<Response<UploadResponse>>
    fun uploadDataToServer(request : VideoDataUpdateRequest) : Observable<Response<SignUpResponse>>
}