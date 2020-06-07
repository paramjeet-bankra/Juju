package com.example.videorecordingapplication.presentation.view.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.presentation.view.videoplayer.CustomPlayerView
import com.example.videorecordingapplication.presentation.view.videoplayer.VideoPlayerControlListener
import com.example.videorecordingapplication.presentation.view.videoplayer.VideoPlayerListener
import com.google.android.exoplayer2.Player

class VerticalScreenSlidePageFragment : Fragment(),
    VideoPlayerControlListener,
    VideoPlayerListener {
    lateinit var playerView: CustomPlayerView
    lateinit var tvFollowing : AppCompatTextView
    lateinit var tvVideoDescription : AppCompatTextView
    lateinit var ivShare : AppCompatImageView
    lateinit var parent : ConstraintLayout
    lateinit var videoData : VideoEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoData = arguments?.getSerializable("video_data") as VideoEntity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vertical_screen_slide_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
        tvFollowing = view.findViewById(R.id.tv_following)
        tvVideoDescription = view.findViewById(R.id.tv_video_description)
        ivShare = view.findViewById(R.id.iv_share)

        tvVideoDescription.setText(videoData.title)
        setVideo(videoData.link)

        ivShare.setOnClickListener {
            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, videoData.link)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }, null)
            startActivity(share)
        }
    }

    private fun setVideo(filePath: String) {
        playerView.onResume()
        playerView.setVideo(filePath)
        playerView.addListener(this)
        playerView.addPlayerControlListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(videoData : VideoEntity) =
            VerticalScreenSlidePageFragment()
                .apply {
                val bundle = Bundle()
                bundle.putSerializable("video_data", videoData)
                arguments = bundle
            }
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
