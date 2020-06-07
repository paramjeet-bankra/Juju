package com.example.videorecordingapplication.presentation.view.videoplayer

interface VideoPlayerListener {
    fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int)
}