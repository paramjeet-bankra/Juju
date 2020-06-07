package com.example.videorecordingapplication.presentation.view.videorecording

interface IRecorderActions {

    fun onStartRecord()

    fun onPauseRecord()

    fun onResumeRecord()

    fun onEndRecord()

    fun onDurationTooShortError()

    fun onSingleTap()

    fun onCancelled()

    fun getCurrentRecorderState() : RecorderStateManager.RecorderState
}
