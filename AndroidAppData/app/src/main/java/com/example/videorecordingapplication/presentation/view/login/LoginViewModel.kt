package com.example.videorecordingapplication.presentation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.videorecordingapplication.data.entity.*
import com.example.videorecordingapplication.data.entity.request.LoginRequest
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.localdatasource.UserData
import com.example.videorecordingapplication.data.repository.RecommendationRepositoryImpl
import com.example.videorecordingapplication.data.repository.SchoolListRepositoryImpl
import com.example.videorecordingapplication.data.repository.SignUpRepositoryImpl
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationViewModel
import com.google.gson.annotations.SerializedName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.security.MessageDigest

class LoginViewModel : ViewModel(){

    private var loginData : LoginRequest? = null
    private var errorState = MutableLiveData<ErrorState>(ErrorState(false, ""))
    private val loginResponseLD = MutableLiveData<LoginResponse>()

    fun observeLoginResponse() : LiveData<LoginResponse>{
        return loginResponseLD
    }

    fun updateLoginResponse(loginResponse: LoginResponse?){
       errorState.postValue(ErrorState(true, ""))
        this.loginResponseLD.postValue(loginResponse)
    }

    fun observeErrorState() : LiveData<ErrorState>{
        return errorState
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
        UserData.name = response.name
        UserData.studentId = response.studentId
        UserData.age = response.age
        UserData.mentorId = response.mentorId
        UserData.mentorName = response.mentorName
        UserData.schoolId = response.schoolId
        updateLoginResponse(response)
    }

    fun updateLoginData(loginRequest : LoginRequest){
        UserData.type = loginRequest.type
        this.loginData = loginRequest
        login()
    }

    private fun onFailure(t: String?) {
        errorState.postValue(ErrorState(false, t))
        Log.d(DataSource.LOG_TAG, "Response failed $t")
    }
}

