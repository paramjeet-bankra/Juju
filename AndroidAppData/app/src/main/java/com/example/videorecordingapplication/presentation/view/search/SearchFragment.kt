package com.example.videorecordingapplication.presentation.view.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.presentation.view.recommendation.RecommendationViewModel

class SearchFragment : Fragment() {
    private lateinit var viewModel : SearchViewModel
    lateinit var rvVerticalList : RecyclerView
    lateinit var progress : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        viewModel.fetchAllVideoList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.title = getString(R.string.title_search)
        progress = view.findViewById(R.id.progress)
        progress.visibility = View.VISIBLE
        rvVerticalList = view.findViewById(R.id.rv_vertical)
        rvVerticalList.layoutManager = LinearLayoutManager(requireContext())
        observeList()
    }
    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private fun observeList(){
        viewModel.observeVideoList().observe(requireActivity(), Observer {
            if (it.size > 0) {
                setAdapter(it as ArrayList<VideoListEntity>)
            }
            progress.visibility = View.GONE
        })
    }

    private fun setAdapter(videoList : ArrayList<VideoListEntity>){
        rvVerticalList.adapter = VerticalListAdapter(requireContext(), videoList)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SearchFragmentInterationListener)
            (context as SearchFragmentInterationListener).onSearchFragmentVisible()
    }

    interface SearchFragmentInterationListener{
        fun onSearchFragmentVisible()
    }

}
