package com.example.videorecordingapplication.presentation.view.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.data.entity.*
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
    private var errorState = MutableLiveData<ErrorState>(ErrorState(false, ""))

    init {
        fetchVideoList()
    }

    private fun updateVideoList(list : List<VideoEntity>){
        this.videoListLD.postValue(list)
    }

    fun observeErrorState() : LiveData<ErrorState>{
        return errorState
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
        errorState.postValue(ErrorState(false, t))
    }

    private fun onResponse(response: ArrayList<VideoListEntity>) {
        errorState.postValue(ErrorState(true, ""))
        updateVideoList(response.get(0).videos)
    }

    private fun onResponse(response: SignUpResponse) {
    }

}