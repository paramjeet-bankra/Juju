package com.example.videorecordingapplication.presentation.view.videoplayer

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class CustomPlayerView : PlayerView, VideoPlayer {

    private var shouldAutoPlay = true
    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    constructor(context: Context) : super(context) {
        createPlayer()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        createPlayer()
    }

    private fun createPlayer() {
        this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        createMp4SupportPlayer()
    }

    private fun createMp4SupportPlayer() {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MOVIE)
            .build()

        val simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        simpleExoPlayer.setAudioAttributes(audioAttributes, true)

        player = simpleExoPlayer
        (player as SimpleExoPlayer).playWhenReady = true
        (player as SimpleExoPlayer).addListener(object : Player.EventListener {

        })

        dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "VideoPlayerApplication")
        )
    }

    private fun playInternal() {
        player!!.playWhenReady = true
        player!!.playbackState
    }

    private fun pauseInternal() {
        player!!.playWhenReady = false
        player!!.playbackState
    }

    override fun release() {
        player?.apply {
            release()
            player = null
        }
    }

    override fun addListener(playerListener: VideoPlayerListener) {
        player?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                playerListener.onPlayerStateChanged(playWhenReady, playbackState)
            }
        })
    }

    override fun addPlayerControlListener(playerControlListener: VideoPlayerControlListener) {
        setControlDispatcher(object : DefaultControlDispatcher() {
            override fun dispatchSetPlayWhenReady(player: Player, playWhenReady: Boolean): Boolean {
                if(playWhenReady) {
                    playerControlListener.onPlayClick()
                } else {
                    playerControlListener.onPauseClick()
                }
                return super.dispatchSetPlayWhenReady(player, playWhenReady)
            }
        })
    }

    override fun play() {
        playInternal()
    }

    override fun pause() {
        pauseInternal()
    }

    override fun setVideo(playUrl: String) {
        setVideoPlayUrl(playUrl)
    }

    private fun setVideoPlayUrl(playUrl: String) {
        setMP4Video(playUrl)

        if (shouldAutoPlay) {
            playInternal()
        } else {
            pauseInternal()
        }
    }

    private fun setMP4Video(playUrl: String) {
        player?.apply {
            val videoSource =
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(playUrl))
            (player as SimpleExoPlayer?)?.prepare(videoSource)
        }
    }

}