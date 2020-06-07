package com.example.videorecordingapplication.presentation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.repository.RecommendationRepositoryImpl
import com.example.videorecordingapplication.data.repository.SchoolListRepositoryImpl
import com.example.videorecordingapplication.data.repository.SignUpRepositoryImpl
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StudentSignUpViewModel : ViewModel() {

    private val schoolLiveData = MutableLiveData<List<SchoolEntity>>()
    private var signUpData: StudentSignUp? = null
    private val signUpResponseLD = MutableLiveData<SignUpResponse>()


    init {
        fetchSchoolList()
    }

    private fun updateSchoolList(list: ArrayList<SchoolEntity>?) {
        this.schoolLiveData.postValue(list)
    }

    fun observeSchoolList(): LiveData<List<SchoolEntity>> {
        return schoolLiveData
    }

    private fun updateStudentResponse(list: SignUpResponse?) {
        this.signUpResponseLD.postValue(list)
    }

    fun observeSignUpResponse(): LiveData<SignUpResponse> {
        return signUpResponseLD
    }

    private fun fetchSchoolList() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            SchoolListRepositoryImpl().getSchoolList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.getValue() != null) {
                        onResponse(response.getValue()!!)
                    } else {
                        onFailure(response.getError()?.message)
                    }
                }, { t ->
                    onFailure(t.message)
                })
        )

    }

    fun registerStudent() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            SignUpRepositoryImpl().registerStudent(signUpData!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.getValue() != null) {
                        onResponse(response.getValue()!!)
                    } else {
                        onFailure(response.getError()?.message)
                    }
                }, { t ->
                    onFailure(t.message)
                })
        )
    }

    private fun onResponse(response: SignUpResponse) {
        Log.d(DataSource.LOG_TAG, "student Signup Response successful")

        updateStudentResponse(response)
    }

    fun updateSignUpData(signUpData: StudentSignUp) {
        this.signUpData = signUpData
        registerStudent()
    }

    private fun onFailure(errorMessage: String?) {
        Log.d(DataSource.LOG_TAG, "student Response failed $errorMessage")
    }

    private fun onResponse(response: ArrayList<SchoolEntity>?) {
        Log.d(DataSource.LOG_TAG, "student School Response successful")

        updateSchoolList(response)
    }
}