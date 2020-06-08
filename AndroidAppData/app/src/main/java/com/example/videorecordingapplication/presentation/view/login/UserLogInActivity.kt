package com.example.videorecordingapplication.presentation.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.data.entity.request.LoginRequest
import com.example.videorecordingapplication.data.entity.request.StudentSignUp
import com.example.videorecordingapplication.presentation.view.moderatorhome.ModeratorHomeActivity
import com.example.videorecordingapplication.presentation.view.recommendation.CategoryRecommendationActivity
import com.example.videorecordingapplication.presentation.view.signup.StudentSignUpViewModel
import com.example.videorecordingapplication.presentation.view.signup.UserSignUpActivity
import com.google.android.material.tabs.TabLayout

class UserLogInActivity : AppCompatActivity() {
    lateinit var tab: TabLayout
    lateinit var etName: AppCompatEditText
    lateinit var etPassword: AppCompatEditText
    lateinit var progress: ProgressBar
    lateinit var btnLogin: AppCompatButton
    lateinit var tvSignUp: AppCompatTextView
    private lateinit var viewModel: LoginViewModel

    var type: String = "student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_in)
        setupViewModel()
        initViews()
        initListeners()
        observeLoginResponse()
        observeErrorState()
    }

    private fun initViews() {
        progress = findViewById(R.id.progress)
        etName = findViewById(R.id.et_login_name)
        etPassword = findViewById(R.id.et_login_password)
        btnLogin = findViewById(R.id.btn_login)
        tvSignUp = findViewById(R.id.tv_login_signup)
        tab = findViewById(R.id.tab_login)

        tab.addTab(tab.newTab().setText(R.string.text_btn_signup_student))
        tab.addTab(tab.newTab().setText(R.string.text_btn_signup_teacher))
    }

    private fun initListeners() {
        btnLogin.setOnClickListener {
            if (etName.text.isNullOrBlank() ||
                etPassword.text.isNullOrBlank()
            ) {
                Toast.makeText(this, "Incorrect Username or password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE
            viewModel.updateLoginData(
                LoginRequest(
                    etName.text.toString(), type, etPassword.text.toString()
                )
            )
        }

        tvSignUp.setOnClickListener {
            finish()
            startActivity(Intent(this, UserSignUpActivity::class.java))
        }

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text.toString().equals(getText(R.string.text_btn_signup_student))) {
                    type = "student"
                } else {
                    type = "mentor"
                }
            }
        })
    }

    private fun observeLoginResponse() {
        viewModel.observeLoginResponse().observe(this, Observer {
            startActivity(
                when (type) {
                    "student" -> Intent(this, CategoryRecommendationActivity::class.java)
                    "mentor" -> Intent(this, ModeratorHomeActivity::class.java)
                    else -> Intent(this, CategoryRecommendationActivity::class.java)
                }
            )
            finish()
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
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }
}
