package com.example.videorecordingapplication.presentation.view.videorecording

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.presentation.view.videorecording.RecorderStateManager

class MediaControllerViewModel : ViewModel() {

    private val recorderState
            = MutableLiveData<RecorderStateManager.RecorderState>()
    private val isDurationShort
            = MutableLiveData<Boolean>()

    fun isDurationShort(): MutableLiveData<Boolean> {
        return isDurationShort
    }

    fun updateShortDuration(isDurationShort: Boolean) {
        this.isDurationShort.value = isDurationShort
    }

    fun getRecorderState(): RecorderStateManager.RecorderState {
        return recorderState.value!!
    }

    fun updateRecordStatus(newState: RecorderStateManager.RecorderState) {
        recorderState.value = newState
    }

    fun getVideoRecorderCurrentState(): MutableLiveData<RecorderStateManager.RecorderState> {
        return recorderState
    }
}
