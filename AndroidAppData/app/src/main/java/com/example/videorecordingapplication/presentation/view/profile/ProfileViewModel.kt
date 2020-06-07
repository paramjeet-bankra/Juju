package com.example.videorecordingapplication.presentation.view.profile

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.data.entity.request.FilterRequest
import com.example.videorecordingapplication.data.entity.request.UserVideosRequest
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.localdatasource.UserData
import com.example.videorecordingapplication.data.repository.RecommendationRepositoryImpl
import com.example.videorecordingapplication.data.repository.VideoListRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProfileViewModel : ViewModel(){

    private val videoListLD = MutableLiveData<List<VideoEntity>>()

    fun updateVideoList(list : ArrayList<VideoEntity>){
        this.videoListLD.postValue(list)
    }

    fun observeVideoList() : LiveData<List<VideoEntity>>{
        return videoListLD
    }

    fun fetchVideoList(userId : Int){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            VideoListRepositoryImpl().getUserVideos(UserVideosRequest("student",userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.getValue() != null) {
                        onResponse(it.getValue()!!)
                    } else {
                        onFailure(it.getError()?.message)
                    }
                }, {
                    t -> onFailure(t.message)
                }))

    }

        private fun onFailure(t: String?) {
            Log.d(DataSource.LOG_TAG, "Video list Response failed $t")
        }

        private fun onResponse(response: ArrayList<VideoEntity>) {
            Log.d(DataSource.LOG_TAG, "Video list Response successful")

            if (UserData.videoList.size > 0)
                response.addAll(UserData.videoList)

            updateVideoList(response)
        }


}