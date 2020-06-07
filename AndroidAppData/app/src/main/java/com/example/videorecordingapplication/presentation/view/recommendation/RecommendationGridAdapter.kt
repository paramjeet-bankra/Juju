package com.example.videorecordingapplication.presentation.view.recommendation

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.presentation.view.GlideApp
import java.util.*
import kotlin.collections.ArrayList

class RecommendationGridAdapter(
    var context: Context,
    val listener : CheckSelectLListener
) : RecyclerView.Adapter<RecommendationGridAdapter.RecommendationVH>() {

    interface CheckSelectLListener{
        fun onCheckSelected(selected : Boolean, categoryData: CategoryData)
    }

    private var recommendationList: ArrayList<CategoryData> = ArrayList()

    fun setList(recommendationList: ArrayList<CategoryData>){
        this.recommendationList.clear()
        this.recommendationList.addAll(recommendationList)
        notifyDataSetChanged()
    }

    inner class RecommendationVH(view: View) : RecyclerView.ViewHolder(view) {
        val cbSelectCategory : AppCompatCheckBox = view.findViewById(R.id.cb_select_category)
        val tvCategoryName : AppCompatTextView = view.findViewById(R.id.tv_category_name)
        val ivCategory : AppCompatImageView = view.findViewById(R.id.iv_category_name)
        val parentView : ConstraintLayout = view.findViewById(R.id.item_parent)

        init {
            cbSelectCategory.setOnCheckedChangeListener { buttonView, isChecked ->
                listener.onCheckSelected(isChecked, recommendationList.get(layoutPosition))}
        }

        fun bind(categoryData: CategoryData){
            tvCategoryName.setText(categoryData.name)

            GlideApp.with(context)
                .load(categoryData.imageurl)
                .into(ivCategory);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationVH {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_recommendation_item,
            parent, false)
        return RecommendationVH(view)
    }

    override fun getItemCount(): Int = recommendationList.size

    override fun onBindViewHolder(holder: RecommendationVH, position: Int) {
        holder.bind(recommendationList.get(position))
    }
}