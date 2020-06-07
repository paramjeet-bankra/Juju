package com.example.videorecordingapplication.data.localdatasource

import com.example.videorecordingapplication.data.entity.VideoDataUpdateRequest
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.request.VideoUploadRequest

object UserData {
    var counter : Int = 0
    var mentorName: String = ""
    var schoolId: Int = 0
    var age: Int = 0
    var name : String = ""
    var studentId : Int = 0
    var type : String = ""
    var mentorId : Int = 0

    var videoList : ArrayList<VideoEntity> = ArrayList()
}