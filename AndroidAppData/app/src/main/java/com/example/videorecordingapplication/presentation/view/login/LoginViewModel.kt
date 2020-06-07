package com.example.videorecordingapplication.presentation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.LoginResponse
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.request.LoginRequest
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.repository.RecommendationRepositoryImpl
import com.example.videorecordingapplication.data.repository.SchoolListRepositoryImpl
import com.example.videorecordingapplication.data.repository.SignUpRepositoryImpl
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel(){

    private var loginData : LoginRequest? = null
    private val loginResponseLD = MutableLiveData<LoginResponse>()

    fun observeLoginResponse() : LiveData<LoginResponse>{
        return loginResponseLD
    }

    fun updateLoginResponse(loginResponse: LoginResponse?){
        this.loginResponseLD.postValue(loginResponse)
    }

    fun login(){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            SignUpRepositoryImpl().login(loginData!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response ->
                    if (response.getValue() != null) {
                        onResponse(response.getValue()!!)
                    } else {
                        onFailure(response.getError()?.message)
                    }
                }, {
                        t -> onFailure(t.message)
                }))
    }

    private fun onResponse(response: LoginResponse) {
        Log.d(DataSource.LOG_TAG, "Login Response successful")
        updateLoginResponse(response)
    }

    fun updateLoginData(loginRequest : LoginRequest){
        this.loginData = loginRequest
        login()
    }

    private fun onFailure(t: String?) {
        Log.d(DataSource.LOG_TAG, "Response failed $t")
    }
}