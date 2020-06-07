package com.example.videorecordingapplication.data.client

import com.example.videorecordingapplication.data.entity.*
import com.example.videorecordingapplication.data.entity.request.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface UploadApiServiceClient {
    @GET("categories/fetch")
    fun getCategories() : Observable<Response<ArrayList<CategoryData>>>

    @GET("schoollist/fetch")
    fun getSchoolList() : Observable<Response<ArrayList<SchoolEntity>>>

    @POST("videos/fetch")
    fun getCategorySpecificVideos(@Body filterRequest : FilterRequest) : Observable<Response<ArrayList<VideoListEntity>>>

    @POST("videos/fetch")
    fun getAllVideos() : Observable<Response<ArrayList<VideoListEntity>>>

    @POST("uservideos/fetch")
    fun getUserSpecificVideos(@Body filterRequest : UserVideosRequest) : Observable<Response<ArrayList<VideoEntity>>>

    @POST("moderate/video")
    fun videoApproval(@Body moderatorRequest : ModeratorRequest) : Observable<Response<SignUpResponse>>

    @POST("register/mentor")
    fun registerMentor(@Body signUp : ModeratorSignUp) : Observable<Response<SignUpResponse>>

    @POST("user/login")
    fun loginUser(@Body loginRequest: LoginRequest) : Observable<Response<LoginResponse>>

    @POST("registerstudent/student")
    fun registerStudent(@Body studentSignUp: StudentSignUp) : Observable<Response<SignUpResponse>>

    @POST("video/uploadVideo")
    fun updateDataToServer(@Body request: VideoDataUpdateRequest) : Observable<Response<SignUpResponse>>

    @Multipart
    @POST("{key}")
    fun uploadData(
        @Path("key") key : String,
        @Part("desc") description: RequestBody,
        @Part file: MultipartBody.Part): Observable<Response<UploadResponse>>


}