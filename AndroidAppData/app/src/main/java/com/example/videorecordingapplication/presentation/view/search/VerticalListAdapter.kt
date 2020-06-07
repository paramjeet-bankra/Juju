package com.example.videorecordingapplication.presentation.view.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.SampleVideoData
import com.example.videorecordingapplication.data.entity.VideoListEntity

private val viewPool = RecyclerView.RecycledViewPool()
class VerticalListAdapter(val context : Context,
                            val videoList : ArrayList<VideoListEntity>)
    : RecyclerView.Adapter<VerticalListAdapter.VerticalListVH>(){

    inner class VerticalListVH(view : View) : RecyclerView.ViewHolder(view) {
        val tvCategoryName : AppCompatTextView = view.findViewById(R.id.tv_category)
        val rvVideoList : RecyclerView = view.findViewById(R.id.rv_horizontal)

        init {
            rvVideoList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        }

        fun bind(videoData : VideoListEntity){
            //use glide to show image
            tvCategoryName.setText("(#) ${videoData.name}")
            rvVideoList.adapter = HorizontalListAdapter(context, videoData.videos)
            rvVideoList.setRecycledViewPool(viewPool)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalListVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_vertical_item, parent, false)
        return VerticalListVH(view)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: VerticalListVH, position: Int) {
        holder.bind(videoList.get(position))
    }
}