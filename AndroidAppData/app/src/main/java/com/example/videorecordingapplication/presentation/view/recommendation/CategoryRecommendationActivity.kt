package com.example.videorecordingapplication.presentation.view.recommendation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.presentation.view.bottomnavigation.BottomNavigationActivity
import kotlinx.android.synthetic.main.activity_category_recommendation.*


class CategoryRecommendationActivity : AppCompatActivity(), RecommendationGridAdapter.CheckSelectLListener {
    private lateinit var viewModel : RecommendationViewModel
    private lateinit var recommendationGridAdapter: RecommendationGridAdapter
    private lateinit var btnDone : AppCompatButton
    private lateinit var progress : ProgressBar
    private lateinit var rvRecommendation : RecyclerView
    private val selectedCatList = ArrayList<CategoryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_recommendation)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(getString(R.string.text_recommendation_title))
        setSupportActionBar(toolbar)
        setupUI()
        initListener()
        setupViewModel()
        observeList()
        observeErrorState()
    }

    private fun observeList(){
        viewModel.observeCategoryList().observe(this, Observer {
            recommendationGridAdapter.setList(it as ArrayList<CategoryData>)
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
            rvRecommendation.visibility = View.VISIBLE
        })
    }

    private fun setupUI(){
        val descritption = findViewById<AppCompatTextView>(R.id.tv_recommendation_description)
        progress = findViewById(R.id.progress_recommendation)

        recommendationGridAdapter =  RecommendationGridAdapter(this, this)
        rvRecommendation = findViewById<RecyclerView>(R.id.rv_recommendation)
        rvRecommendation.layoutManager = GridLayoutManager(this, 2)
        rvRecommendation.adapter = recommendationGridAdapter

        progress.visibility = View.VISIBLE
        rvRecommendation.visibility = View.GONE

        btnDone = findViewById(R.id.btn_recommendation_done)
    }

    private fun initListener(){
        btnDone.setOnClickListener {
            if (selectedCatList.size > 0) {
                finish()
                val intent = Intent(this, BottomNavigationActivity::class.java)
                intent.putExtra("category", selectedCatList.get(0).id)
                startActivity(intent)
            } else{
                Toast.makeText(this, "Please select one or more categories", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(RecommendationViewModel::class.java)
    }

    override fun onCheckSelected(selected: Boolean, categoryData: CategoryData) {
        if (selected)
            selectedCatList.add(categoryData)
        else if (selectedCatList.contains(categoryData))
            selectedCatList.remove(categoryData)

    }
}
