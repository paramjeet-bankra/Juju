package com.example.videorecordingapplication.presentation.view.moderatorhome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SampleVideoData
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.presentation.view.homepage.HomeScreenFragment
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationGridAdapter
import com.example.videorecordingapplication.presentation.view.search.ModeratorHomeViewModel
import com.example.videorecordingapplication.presentation.view.search.SearchViewModel
import com.example.videorecordingapplication.presentation.view.search.VerticalListAdapter

class ModeratorHomeActivity : AppCompatActivity(), ModeratorHomeAdapter.DialogSelectionListener, VideoPlayerFragment.OnFragmentInteractionListener {
    private lateinit var recommendationGridAdapter: ModeratorHomeAdapter
    private lateinit var viewModel : ModeratorHomeViewModel
    lateinit var rvRecommendation : RecyclerView
    lateinit var fragmentContainer : FrameLayout
    lateinit var toolbar: Toolbar
    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_recommendation)
        toolbar = findViewById(R.id.toolbar)
        setTitle(R.string.title_moderator)
        setSupportActionBar(toolbar)

        setupViewModel()

        val descritption = findViewById<AppCompatTextView>(R.id.tv_recommendation_description)
        descritption.setText(R.string.text_moderator_desc)

        progress = findViewById(R.id.progress_recommendation)
        progress.visibility = View.VISIBLE
        fragmentContainer = findViewById(R.id.fragment_container)
        rvRecommendation = findViewById<RecyclerView>(R.id.rv_recommendation)
        rvRecommendation.layoutManager = GridLayoutManager(this, 2)
        observeList()
        observeErrorState()

    }

    override fun playClicked(videoEntity: VideoEntity) {
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, VideoPlayerFragment.newInstance(videoEntity))
            .commit()
    }

    private fun observeList(){
        viewModel.observeVideoList().observe(this, Observer {
            if (it.size > 0) {
                recommendationGridAdapter =  ModeratorHomeAdapter(this, this, it as ArrayList<VideoEntity>)
                rvRecommendation.adapter = recommendationGridAdapter
            }
        })
    }

    private fun observeErrorState() {
        viewModel.observeErrorState().observe(this, Observer {
            if (!it.status && !it.message.isNullOrBlank()) {
                if (it.message.equals(getString(R.string.error_string))){
                    Toast.makeText(this, getString(R.string.alternate_error_string), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            progress.visibility = View.GONE
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ModeratorHomeViewModel::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onTickClick(videoId : Int?) {
        videoId?.apply {
            viewModel.approve(videoId)
            fragmentContainer.visibility = View.GONE
        }
    }

    override fun onCrossClick(videoId : Int?) {
        videoId?.apply {
            viewModel.reject(videoId)
            fragmentContainer.visibility = View.GONE
        }
    }

    override fun fragmentOpen() {
        TODO("Not yet implemented")
    }
}
