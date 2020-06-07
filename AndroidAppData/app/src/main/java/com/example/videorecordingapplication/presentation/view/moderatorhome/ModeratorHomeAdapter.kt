package com.example.videorecordingapplication.presentation.view.moderatorhome

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SampleVideoData
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.presentation.view.GlideApp
import com.example.videorecordingapplication.presentation.view.homepage.HomeScreenFragment
import java.io.File

class ModeratorHomeAdapter(val context : Context,
                           val listrner : DialogSelectionListener,
                            val videoList : ArrayList<VideoEntity>)
    : RecyclerView.Adapter<ModeratorHomeAdapter.ModeratorHomeVH>(){

    inner class ModeratorHomeVH(view : View) : RecyclerView.ViewHolder(view) {
        val ivThumbnail : AppCompatImageView = view.findViewById(R.id.iv_thumbnail)
        val ivPlay : AppCompatImageView = view.findViewById(R.id.ib_play)

        init {
            ivPlay.setOnClickListener {
               listrner.playClicked(videoList.get(layoutPosition))
            }
        }

        fun bind(videoData : VideoEntity){
            if (!TextUtils.isEmpty(videoData.thumbnail)){
                GlideApp.with(context)
                    .load(videoData.thumbnail)
                    .into(ivThumbnail);
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeratorHomeVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_moderator_home, parent, false)
        return ModeratorHomeVH(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ModeratorHomeVH, position: Int) {
        holder.bind(videoList.get(position))
    }

    interface DialogSelectionListener{
        fun playClicked(videoEntity : VideoEntity)
    }
}