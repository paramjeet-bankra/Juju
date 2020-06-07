package com.example.videorecordingapplication.presentation.view.homepage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.data.entity.VideoEntity
import com.example.videorecordingapplication.data.entity.VideoListEntity
import com.example.videorecordingapplication.data.localdatasource.DataSource
import com.example.videorecordingapplication.presentation.view.search.SearchViewModel

class HomeScreenFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    lateinit var mPager: ViewPager2
    lateinit var progress: ProgressBar
    lateinit var ivCross : AppCompatImageView
    lateinit var videoArray: Array<String>
    var category: Int? = 0
    var videoData: VideoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()

        category = arguments?.getInt("category")
       // videoData = arguments?.getSerializable("video") as VideoEntity
        if (videoData == null) {
            if (category == null)
                viewModel.fetchVideoList(3)
            else {
                viewModel.fetchVideoList(category!!)
            }
        } else {
            val list = ArrayList<VideoListEntity>()
            val entityList = ArrayList<VideoEntity>()
            entityList.add(videoData!!)
            list.add(VideoListEntity(0, "", entityList))
            viewModel.updateVideoList(list)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private fun observeList() {
        viewModel.observeVideoList().observe(requireActivity(), Observer { list ->

            if (list.size > 0) {
                progress.visibility = View.GONE
                mPager.adapter = object : FragmentStateAdapter(this) {
                    override fun getItemCount(): Int = list.get(0).videos.size

                    override fun createFragment(position: Int): Fragment =
                        VerticalScreenSlidePageFragment.newInstance(
                            list.get(0).videos.get(position)
                        )
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.title = getString(R.string.title_home)

        progress = view.findViewById(R.id.progress_home)
        progress.visibility = View.VISIBLE
        mPager = view.findViewById(R.id.pager)
        mPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        ivCross = view.findViewById(R.id.cross)
        ivCross.visibility = View.GONE

        observeList()
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryId: Int?, videoEntity: VideoEntity?) =
            HomeScreenFragment()
                .apply {
                    val bundle = Bundle()
                    categoryId?.apply {
                        bundle.putInt("category", categoryId)
                    }
                    videoEntity?.apply {
                        bundle.putSerializable("video", videoEntity)
                    }

                    arguments = bundle
                }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeFragmentInterationListener)
            (context as HomeFragmentInterationListener).onHomeFragmentVisible()
    }

    interface HomeFragmentInterationListener{
        fun onHomeFragmentVisible()
    }
}
