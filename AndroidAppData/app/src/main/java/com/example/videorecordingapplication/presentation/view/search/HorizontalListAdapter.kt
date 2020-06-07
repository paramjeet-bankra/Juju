package com.example.videorecordingapplication.presentation.view.search

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SampleVideoData
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.presentation.view.GlideApp
import com.example.videorecordingapplication.presentation.view.GlideOptions
import java.io.File

class HorizontalListAdapter(val context : Context,
                            val videoList : ArrayList<VideoEntity>)
    : RecyclerView.Adapter<HorizontalListAdapter.HorizontalListVH>(){

    inner class HorizontalListVH(view : View) : RecyclerView.ViewHolder(view) {
        val ivThumbnail : AppCompatImageView = view.findViewById(R.id.iv_thumbnail)
        val ivPlay : AppCompatImageView = view.findViewById(R.id.ib_play)
        val ivMenu : AppCompatImageView = view.findViewById(R.id.iv_menu)
        val parent : ConstraintLayout = view.findViewById(R.id.parent_view)

        init {
            ivPlay.visibility = View.GONE
            ivMenu.visibility = View.GONE

            parent.setOnClickListener {
                //start home fragment
            }
        }

        fun bind(videoData : VideoEntity){
            if (!TextUtils.isEmpty(videoData.thumbnail)) {
                GlideApp.with(context)
                    .load(videoData.thumbnail)
                    .into(ivThumbnail)
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalListVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_horizotal_item, parent, false)
        return HorizontalListVH(view)
    }

    override fun getItemCount(): Int {
       return videoList.size
    }

    override fun onBindViewHolder(holder: HorizontalListVH, position: Int) {
       holder.bind(videoList.get(position))
    }
}