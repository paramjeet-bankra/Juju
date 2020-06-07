package com.example.videorecordingapplication.presentation.view.videopost

import android.R.attr.description
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.videorecordingapplication.data.entity.UploadResponse
import com.example.videorecordingapplication.data.entity.VideoDataUpdateRequest
import com.example.videorecordingapplication.data.entity.request.VideoUploadRequest
import com.example.videorecordingapplication.data.repository.VideoListRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class VideoPostViewModel : ViewModel(){

    var videoUrl : String?=null
    var imageUrl : String?=null
    var localImageUrl : String?=null

    fun uploadData(key : String, body : MultipartBody.Part, desc : RequestBody ){
            val compositeDisposable = CompositeDisposable()
            compositeDisposable.add(
                VideoListRepositoryImpl().uploadData(key,desc, body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                            response -> if (response.getValue() != null) {
                        onResponse(response.getValue()!!)
                    } else {
                        onFailure(response.getError()?.message)
                    }
                    }, {
                            t -> onFailure(t.message)
                    }))
    }

    private fun onResponse(response: UploadResponse) {
        Log.d("JujuVideoUpload", "video upload successful")
        videoUrl = response.objURL
    }

   /* fun updateVideoDataToServer(){
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            VideoListRepositoryImpl().uploadDataToServer(VideoDataUpdateRequest())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                        response -> if (response.getValue() != null) {
                    onResponse(response.getValue()!!)
                } else {
                    onFailure(response.getError()?.message)
                }
                }, {
                        t -> onFailure(t.message)
                }))
    }*/

    private fun onFailure(t: String?) {
        Log.d("JujuVideoUpload", "video upload failed $t")
    }

    fun extractFrames(uri: String?) {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(uri)

        val bitmaps = arrayOfNulls<Bitmap>(2)

        var index = 0

        /*
        * 1000 is multiplied with video duration to convert duration to microseconds as
        * MediaMetaDataRetriever.getFrameAtTime accepts time in microseconds
        */
        val videoDuration = (mediaMetadataRetriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION)).toLong() * 1000

        val start = ((10 * videoDuration) / 100)
        bitmaps[index] = mediaMetadataRetriever.getFrameAtTime(start)
        bitmaps[index]?.let { localImageUrl = getOutputMediaFile(it) }

        clearBitmaps()
        setBitmaps(bitmaps)
        mediaMetadataRetriever.release()
    }

    private var bitmaps: Array<Bitmap?>? = null

    fun setBitmaps(bitmaps: Array<Bitmap?>?) {
        this.bitmaps = bitmaps
    }

    fun clearBitmaps() {
        if (bitmaps != null && bitmaps!!.size > 0)
            for (bitmap in bitmaps!!) {
                bitmap?.recycle()
            }
        bitmaps = null
    }

    fun getOutputMediaFile(finalBitmap: Bitmap): String? {
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
        var file = File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")

        val out = FileOutputStream(file)
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()

        return file.absolutePath
    }

}