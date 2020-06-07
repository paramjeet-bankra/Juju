package com.example.videorecordingapplication.presentation.view.signup

import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SchoolEntity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SchoolListAdapter(private val context : Context,
                        val listener : OnItemClickListener,
                        val schoolList: ArrayList<SchoolEntity>) : RecyclerView.Adapter<SchoolListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.school_name_pop_up_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(schoolList[position])
    }

    override fun getItemCount(): Int {
        return schoolList.size
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvTitle : AppCompatTextView = itemView.findViewById(R.id.tv_list_item)

        init {
            itemView.setOnClickListener { listener.onItemClick(schoolList.get(layoutPosition)) }
        }

        fun bind(schoolEntity: SchoolEntity) {
            tvTitle.text = schoolEntity.name
        }
    }

    interface OnItemClickListener{
        fun onItemClick(school : SchoolEntity)
    }
}