package com.example.videorecordingapplication.presentation.view.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.data.entity.request.FilterRequest
import com.example.videorecordingapplication.data.entity.request.ModeratorRequest
import com.example.videorecordingapplication.data.entity.request.UserVideosRequest
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.repository.ModeratorRepositoryImpl
import com.example.videorecordingapplication.data.repository.RecommendationRepositoryImpl
import com.example.videorecordingapplication.data.repository.VideoListRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ModeratorHomeViewModel : ViewModel(){

    private val videoListLD = MutableLiveData<List<VideoEntity>>()

    init {
        fetchVideoList()
    }

    private fun updateVideoList(list : List<VideoEntity>){
        this.videoListLD.postValue(list)
    }


    fun observeVideoList() : LiveData<List<VideoEntity>>{
        return videoListLD
    }

    fun reject(videoId : Int){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ModeratorRepositoryImpl().videoapprove(ModeratorRequest("reject",videoId))
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

    fun approve(videoId : Int){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ModeratorRepositoryImpl().videoapprove(ModeratorRequest("active",videoId))
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

    private fun fetchVideoList(){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
           // VideoListRepositoryImpl().getUserVideos(UserVideosRequest("mentor",2))
            VideoListRepositoryImpl().getAllVideos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                        response ->
                    if (response.getValue() != null) {
                        onResponse(response.getValue()!!)
                    } else {
                        onFailure(response.getError()?.message)
                    }
                }, {
                        t -> onFailure(t.message)
                }))

    }

    private fun onFailure(t: String?) {
        Log.d(DataSource.LOG_TAG, "Response Failed $t")
    }

    private fun onResponse(response: ArrayList<VideoListEntity>) {
        Log.d(DataSource.LOG_TAG, "Video List for moderator successful")

        //response.filter { it -> "pending" == it.status }
        updateVideoList(response.get(0).videos)
    }

    private fun onResponse(response: SignUpResponse) {
        Log.d(DataSource.LOG_TAG, "Sign-up Response successful")

    }

}