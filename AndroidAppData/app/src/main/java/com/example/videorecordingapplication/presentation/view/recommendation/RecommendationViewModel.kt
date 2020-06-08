package com.example.videorecordingapplication.presentation.view.recommendation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.ErrorState
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.repository.RecommendationRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecommendationViewModel : ViewModel(){

    private val categoryListLiveData = MutableLiveData<List<CategoryData>>()
    private var errorState = MutableLiveData<ErrorState>(ErrorState(false, ""))

    init {
        fetchCategoryList()
    }

    private fun updateCategoryList(list : ArrayList<CategoryData>){
        this.categoryListLiveData.postValue(list)
    }

    fun observeCategoryList() : LiveData<List<CategoryData>>{
        return categoryListLiveData
    }

    fun observeErrorState() : LiveData<ErrorState>{
        return errorState
    }

    private fun fetchCategoryList(){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            RecommendationRepositoryImpl().getRecommendations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    response ->  if (response.getValue() != null) {
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
            Log.d(DataSource.LOG_TAG, "Recommendation Response failed $t")
        }

        private fun onResponse(response: ArrayList<CategoryData>) {
            Log.d(DataSource.LOG_TAG, "Recommendation Response successful")
            errorState.postValue(ErrorState(true, ""))
            updateCategoryList(response)
        }

}