package com.example.videorecordingapplication.presentation.view.videoplayer

interface VideoPlayer {
    fun setVideo(playUrl: String)
    fun play()
    fun pause()
    fun release()
    fun addListener(playerListener: VideoPlayerListener)
    fun addPlayerControlListener(playerControlListener: VideoPlayerControlListener)
}