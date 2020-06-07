package com.example.videorecordingapplication.data.remotedatasource

import com.example.videorecordingapplication.data.client.ApiServiceClient
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    val service : ApiServiceClient
    var baseUrl = "https://f60a7503.eu-gb.apigw.appdomain.cloud/"

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()

        service = retrofit.create<ApiServiceClient>(ApiServiceClient::class.java)
    }
}