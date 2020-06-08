package com.example.videorecordingapplication.presentation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.data.entity.ErrorState
import com.example.videorecordingapplication.data.entity.SignUpResponse
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.entity.request.ModeratorSignUp
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.data.repository.SchoolListRepositoryImpl
import com.example.videorecordingapplication.data.repository.SignUpRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TeacherSignUpViewModel : ViewModel() {

    private val schoolLiveData = MutableLiveData<List<SchoolEntity>>()
    private var signUpData: ModeratorSignUp? = null
    private val signUpResponseLD = MutableLiveData<SignUpResponse>()
    private var errorState = MutableLiveData<ErrorState>(ErrorState(false, ""))

    init {
        fetchSchoolList()
    }

    private fun updateSchoolList(list: ArrayList<SchoolEntity>) {
        this.schoolLiveData.postValue(list)
    }

    fun observeSchoolList(): LiveData<List<SchoolEntity>> {
        return schoolLiveData
    }

    private fun updateTeacherResponse(list: SignUpResponse) {
        this.signUpResponseLD.postValue(list)
    }

    fun observeSignUpResponse(): LiveData<SignUpResponse> {
        return signUpResponseLD
    }

    fun observeErrorState() : LiveData<ErrorState>{
        return errorState
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

    fun registerModerator() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            SignUpRepositoryImpl().registerMentor(signUpData!!)
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

    private fun onFailure(t: String?) {
        Log.d(DataSource.LOG_TAG, "teacher Response failed $t")
        errorState.postValue(ErrorState(false, t))
    }

    private fun onResponse(response: ArrayList<SchoolEntity>) {
        Log.d(DataSource.LOG_TAG, "teacher School list Response successful")
        updateSchoolList(response)
    }

    private fun onResponse(response: SignUpResponse) {
        Log.d(DataSource.LOG_TAG, "teacher Signup Response successful")
        errorState.postValue(ErrorState(true, ""))
        updateTeacherResponse(response)
    }

    fun updateSignUpData(signUpData: ModeratorSignUp) {
        this.signUpData = signUpData
        registerModerator()
    }
}