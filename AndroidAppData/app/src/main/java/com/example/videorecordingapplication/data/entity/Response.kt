package com.example.videorecordingapplication.data.entity

import java.lang.Exception

class Response<T>(val data: T?, private val error :String?) {
    fun getValue(): T? {
        return data
    }

    fun getError() : Exception? {
        return Exception(error)
    }
}