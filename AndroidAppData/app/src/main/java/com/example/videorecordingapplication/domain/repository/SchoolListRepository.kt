package com.example.videorecordingapplication.domain.repository

import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.SchoolEntity
import io.reactivex.Observable

interface SchoolListRepository {
    fun getSchoolList() : Observable<Response<ArrayList<SchoolEntity>>>

}