package com.example.videorecordingapplication.presentation.view.videorecording

import android.app.Activity
import android.content.Intent
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProviders
import com.example.videorecordingapplication.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity(), CustomVideoButton.ActionListener{
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var mediaRecorder : MediaRecorder? = null
    var isRecording = false
    private val TAG = "CameraActivity"
    lateinit var captureButton: AppCompatButton
    lateinit var btnVideoRecorder: CustomVideoButton
    private lateinit var anim: AlphaAnimation

    private var startTime = 0L
    private var myHandler = Handler()
    private var timeInMillies = 0L
    private var finalPausedTime = 0L
    private var finalTime = 0L

    lateinit var llTimer: LinearLayout
    lateinit var ibFlash: AppCompatImageButton
    lateinit var ibRedDot: AppCompatImageButton
    lateinit var tvTimerText: AppCompatTextView
    lateinit var ibProceed: AppCompatImageButton

    var filePath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        btnVideoRecorder = findViewById(R.id.btn_video_recorder)
        prepareRecorderController()
        llTimer = findViewById(R.id.ll_video_timer)
        ibFlash =findViewById(R.id.ib_flash)
        ibRedDot = findViewById(R.id.ib_red_dot)
        tvTimerText = findViewById(R.id.tv_timer_text)
        ibProceed = findViewById(R.id.ib_proceed)

        ibProceed.setOnClickListener{
            val intent = Intent()
            intent.putExtra("filepath", filePath)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        mCamera = getCameraInstance()

        mPreview = mCamera?.let {
            CameraPreview(this, it)
        }

        mPreview?.also {
            val preview: FrameLayout = findViewById(R.id.camera_preview)
            preview.addView(it)
        }
    }

    private fun startRecording(){
        if (prepareVideoRecorder()) {
            mediaRecorder?.start()
            isRecording = true
        } else {
            releaseMediaRecorder()
        }
    }

    private fun stopRecording(){
        if (isRecording) {
            mediaRecorder?.stop()
            releaseMediaRecorder()
            mCamera?.lock()
            isRecording = false
        }
    }

    private fun prepareRecorderController() {
        initAnimation()
        btnVideoRecorder.setListener(this)
    }

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open()
        } catch (e: Exception) {
            null
        }
    }

    private val mPicture = Camera.PictureCallback { data, _ ->
        val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
            Log.d("CameraActivity", ("Error creating media file, check storage permissions"))
            return@PictureCallback
        }

        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d("CameraActivity", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.d("CameraActivity", "Error accessing file: ${e.message}")
        }
    }


    private fun prepareVideoRecorder(): Boolean {
        mediaRecorder = MediaRecorder()

        mCamera?.let { camera ->
            camera.unlock()

            mediaRecorder?.run {
                setOrientationHint(getCameraOrientation())
                setCamera(camera)

                setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)
                setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
                filePath = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString()
                setOutputFile(filePath)
                setPreviewDisplay(mPreview?.holder?.surface)
                return try {
                    prepare()
                    true
                } catch (e: IllegalStateException) {
                    Log.d(TAG, "IllegalStateException preparing MediaRecorder: ${e.message}")
                    releaseMediaRecorder()
                    false
                } catch (e: IOException) {
                    Log.d(TAG, "IOException preparing MediaRecorder: ${e.message}")
                    releaseMediaRecorder()
                    false
                }
            }

        }
        return false
    }

    override fun onPause() {
        super.onPause()
        releaseMediaRecorder()
        releaseCamera()
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.reset()
        mediaRecorder?.release()
        mediaRecorder = null
        mCamera?.lock()
    }

    private fun releaseCamera() {
        mCamera?.release()
        mCamera = null
    }


    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2

    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "JujuPlus"
        )

        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
            }
            else -> null
        }
    }

    fun getCameraOrientation(): Int {
        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(0, cameraInfo)
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result = 0
        when (0) {
            Camera.CameraInfo.CAMERA_FACING_BACK -> {
                result = (cameraInfo.orientation - degrees + 360) % 360
            }

            Camera.CameraInfo.CAMERA_FACING_FRONT -> {
                result = (cameraInfo.orientation + degrees) % 360
                result = (360 - result) % 360 // compensate the mirror
            }
        }
        return result
    }

    private var updateTimerMethod = object : Runnable {
        override fun run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime
            finalTime = finalPausedTime + timeInMillies
            val seconds = (finalTime / 1000) % 60
            val minutes =  (finalTime / 1000) / 60
            tvTimerText.text = ("" + minutes + ":" + String.format("%02d", seconds))
            myHandler.postDelayed(this, 0)
        }
    }

    private fun initAnimation() {
        anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
    }

    fun startTimer() {
        startBlinking()
        startTime = SystemClock.uptimeMillis()
        Log.d("Timer", "start time $startTime")

        myHandler.postDelayed(updateTimerMethod, 0)
    }

    fun stopTimer() {
        stopBlinking()
        myHandler.removeCallbacks(updateTimerMethod)
    }

    fun pauseTimer() {
        finalPausedTime += timeInMillies;
        stopTimer()
    }

    internal fun startBlinking() {
        ibRedDot.startAnimation(anim)
    }

    internal fun stopBlinking() {
        ibRedDot.clearAnimation()
    }

    override fun onStartRecord() {
        startRecording()
        startTimer()
        ibRedDot.visibility = View.VISIBLE
        ibProceed.visibility = View.GONE
        llTimer.visibility = View.VISIBLE
        ibFlash.visibility = View.VISIBLE
        startTimer()
    }

    override fun onEndRecord() {
        stopRecording()
        stopTimer()
        llTimer.visibility = View.GONE
        ibFlash.visibility = View.GONE
        ibProceed.visibility = View.VISIBLE
        setResult(Activity.RESULT_OK)
    }

    override fun onDurationTooShortError() {

    }

    override fun onSingleTap() {
    }

    override fun onCancelled() {

    }
}