package com.example.videorecordingapplication.presentation.view.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.videorecordingapplication.R
import com.example.videorecordingapplication.presentation.view.signup.UserSignUpActivity

import android.content.Intent;
import android.os.Handler;

import android.view.WindowManager;
import com.example.videorecordingapplication.presentation.view.login.UserLogInActivity

public class MainActivity : AppCompatActivity() {
    companion object{
        const val SPLASH_SCREEN_TIME_OUT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Handler().postDelayed(object : Runnable {

            override fun run() {
                startActivity(Intent(this@MainActivity, UserLogInActivity::class.java))
                finish()
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
