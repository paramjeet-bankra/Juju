package com.example.videorecordingapplication.presentation.view.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.SampleVideoData
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationGridAdapter
import com.example.videorecordingapplication.presentation.view.search.HorizontalListAdapter
import com.example.videorecordingapplication.presentation.view.search.SearchViewModel

class ProfileFragment : Fragment() {
    lateinit var videoList : ArrayList<SampleVideoData>
    private lateinit var recommendationGridAdapter: HorizontalListAdapter
    private lateinit var progress : ProgressBar
    private lateinit var viewModel : ProfileViewModel
    lateinit var rvRecommendation : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        viewModel.fetchVideoList(1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.title = getString(R.string.title_profile)

        progress = view.findViewById(R.id.progress)
        progress.visibility = View.VISIBLE
        rvRecommendation = view.findViewById(R.id.rv_video)
        rvRecommendation.layoutManager = GridLayoutManager(requireContext(), 3)
        observeList()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    private fun observeList(){
        viewModel.observeVideoList().observe(requireActivity(), Observer {
            if (it.size > 0) {
                setAdapter(it as ArrayList<VideoEntity>)
            }

            progress.visibility = View.GONE
        })
    }

    fun setAdapter(videoList : ArrayList<VideoEntity>){
        recommendationGridAdapter =  HorizontalListAdapter(requireContext(), videoList)
        rvRecommendation.adapter = recommendationGridAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragmentInterationListener)
            (context as ProfileFragmentInterationListener).onProfileFragmentVisible()
    }

    interface ProfileFragmentInterationListener{
        fun onProfileFragmentVisible()
    }

}
