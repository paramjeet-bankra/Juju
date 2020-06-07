package com.example.videorecordingapplication.presentation.view.bottomnavigation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.CategoryData
import com.example.videorecordingapplication.presentation.view.homepage.HomeScreenFragment
import com.example.videorecordingapplication.presentation.view.profile.ProfileFragment
import com.example.videorecordingapplication.presentation.view.search.SearchFragment
import com.example.videorecordingapplication.presentation.view.videopost.VideoPostFragment
import com.example.videorecordingapplication.presentation.view.videorecording.CameraActivity

class BottomNavigationActivity : AppCompatActivity(),
    HomeScreenFragment.HomeFragmentInterationListener,
        ProfileFragment.ProfileFragmentInterationListener,
        VideoPostFragment.PostFragmentInterationListener,
        SearchFragment.SearchFragmentInterationListener
{
    private lateinit var ivHome: AppCompatImageView
    private lateinit var ivPost: AppCompatImageView
    private lateinit var ivSearch: AppCompatImageView
    private lateinit var ivProfile: AppCompatImageView

    private val RECORD_REQUEST_CODE = 1000
    private val CAMERA_RECORDING = 1001
    private val TAG = "VideoRecordingApp"

    private var category : Int? = null
    lateinit var toolbar: Toolbar
    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false

    private var permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        category = intent.getIntExtra("category", 0)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        initViews()
        initializeListeners()
        loadFragment()
    }

    private fun loadFragment(){
        openHomeFragment()
    }

    private fun initViews(){
        ivHome = findViewById(R.id.iv_home)
        ivProfile = findViewById(R.id.iv_profile)
        ivSearch = findViewById(R.id.iv_search)
        ivPost = findViewById(R.id.iv_post)
    }


    private fun initializeListeners() {
        ivHome.setOnClickListener {
            openHomeFragment()
        }

        ivPost.setOnClickListener {
            permission()
        }

        ivSearch.setOnClickListener {
            openSearchFragment()
        }

        ivProfile.setOnClickListener {
            openProfileFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_RECORDING -> {
                if (resultCode == Activity.RESULT_OK) {
                    var filePath = data?.getStringExtra("filepath")
                    openPostFragment(filePath)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                    startVideoRecording()
                }
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            RECORD_REQUEST_CODE
        )
    }

    private fun permission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startVideoRecording()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECORD_AUDIO)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the microphone is required for this app to record audio.")
                    .setTitle("Permission required")
                builder.setPositiveButton("OK") { dialog, id ->
                    Log.i(TAG, "Clicked")
                    makeRequest()
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }
    }

    private fun openHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, HomeScreenFragment.newInstance(category, null))
            .commit()
    }

    private fun openSearchFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SearchFragment())
            .commit()
    }

    private fun openProfileFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, ProfileFragment())
            .commit()
    }

    private fun openPostFragment(path : String?) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, VideoPostFragment.newInstance(path))
            .commit()
    }

    private fun startVideoRecording() {
        startActivityForResult(Intent(this, CameraActivity::class.java), 1001)
    }

    override fun onHomeFragmentVisible() {
        toolbar.setTitle(getString(R.string.title_home))
        setSupportActionBar(toolbar)
    }

    override fun onProfileFragmentVisible() {
        toolbar.setTitle(getString(R.string.title_profile))
        setSupportActionBar(toolbar)
    }

    override fun onPostFragmentVisible() {
        toolbar.setTitle(getString(R.string.title_post))
        setSupportActionBar(toolbar)
    }

    override fun onSearchFragmentVisible() {
        toolbar.setTitle(getString(R.string.title_search))
        setSupportActionBar(toolbar)
    }
}
