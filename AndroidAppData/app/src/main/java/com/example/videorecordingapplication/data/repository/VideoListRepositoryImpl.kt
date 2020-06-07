package com.example.videorecordingapplication.data.repository

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.example.videorecordingapplication.data.entity.*
import com.example.videorecordingapplication.data.entity.request.FilterRequest
import com.example.videorecordingapplication.data.entity.request.UserVideosRequest
import com.example.videorecordingapplication.data.entity.request.VideoUploadRequest
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.remotedatasource.ApiService
import com.example.videorecordingapplication.domain.repository.VideoListRepository
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class VideoListRepositoryImpl : VideoListRepository{
    override fun getUserVideos(userVideosRequest: UserVideosRequest): Observable<Response<ArrayList<VideoEntity>>> {
        return ApiService().service.getUserSpecificVideos(userVideosRequest)
    }

    override fun getFilteredVideos(filterRequest: FilterRequest): Observable<Response<ArrayList<VideoListEntity>>> {
        return ApiService().service.getCategorySpecificVideos(filterRequest)
    }

    override fun getAllVideos(): Observable<Response<ArrayList<VideoListEntity>>> {
        return ApiService().service.getAllVideos()
    }

    override fun uploadData(desc : RequestBody, multipart : MultipartBody.Part): Observable<Response<UploadResponse>> {
        return ApiService().service.uploadData(desc, multipart)
    }

    override fun uploadDataToServer(request : VideoDataUpdateRequest): Observable<Response<SignUpResponse>> {
        return ApiService().service.updateDataToServer(request)
    }
}