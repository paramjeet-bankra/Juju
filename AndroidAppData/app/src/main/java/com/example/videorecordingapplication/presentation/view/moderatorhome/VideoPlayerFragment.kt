package com.example.videorecordingapplication.presentation.view.moderatorhome

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView

import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.presentation.view.videoplayer.CustomPlayerView
import com.example.videorecordingapplication.presentation.view.videoplayer.VideoPlayerControlListener
import com.example.videorecordingapplication.presentation.view.videoplayer.VideoPlayerListener
import com.google.android.exoplayer2.Player
import java.time.Clock.tick

class VideoPlayerFragment : Fragment(),
    VideoPlayerControlListener,
    VideoPlayerListener {

    lateinit var tick : AppCompatImageView
    lateinit var cross : AppCompatImageView
    lateinit var listener : OnFragmentInteractionListener
    var videoEntity : VideoEntity? = null
    lateinit var playerView: CustomPlayerView

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         videoEntity = arguments?.getSerializable("video") as VideoEntity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tick = view.findViewById(R.id.tick)
        cross = view.findViewById(R.id.cross)
        playerView = view.findViewById(R.id.player_view)
        setVideo(videoEntity?.link)

        tick.setOnClickListener {
            listener.onTickClick(videoEntity?.videoId)
        }

        cross.setOnClickListener {
            listener.onCrossClick(videoEntity?.videoId)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(videoEntity: VideoEntity) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("video", videoEntity)
                }
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
           listener = (context as OnFragmentInteractionListener)
    }

    interface OnFragmentInteractionListener{
        fun onTickClick(videoId : Int?)
        fun onCrossClick(videoId : Int?)
        fun fragmentOpen()
    }


    override fun onPlayClick() {
        TODO("Not yet implemented")
    }

    override fun onPauseClick() {
        pausePlayer()
    }


    override fun onPause() {
        pausePlayer()
        super.onPause()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playWhenReady) {
            acquireWakeLock()
        } else {
            releaseWakeLock()
        }
        when(playbackState){
            Player.STATE_ENDED ->{
                //scroll next
            }
        }
    }

    private fun setVideo(filePath: String?) {
        filePath?.apply {
            playerView.onResume()
            playerView.setVideo(filePath)
            playerView.addListener(this@VideoPlayerFragment)
            playerView.addPlayerControlListener(this@VideoPlayerFragment)
        }

    }

    private fun pausePlayer() {
        playerView.pause()
        playerView.onPause()

        releaseWakeLock()
    }

    override fun onDestroyView() {
        playerView.onPause()
        playerView.release()
        super.onDestroyView()
    }

    override fun onStop() {
        releaseWakeLock()
        super.onStop()
    }

    fun acquireWakeLock() {
        activity?.apply {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    fun releaseWakeLock() {
        activity?.apply {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
