package com.example.videorecordingapplication.data.repository

import com.example.videorecordingapplication.data.entity.Response
import com.example.videorecordingapplication.data.entity.SchoolEntity
import com.example.videorecordingapplication.data.remotedatasource.ApiService
import com.example.videorecordingapplication.domain.repository.SchoolListRepository
import io.reactivex.Observable

class SchoolListRepositoryImpl : SchoolListRepository {
    override fun getSchoolList(): Observable<Response<ArrayList<SchoolEntity>>> {
        return ApiService().service.getSchoolList()
    }
}