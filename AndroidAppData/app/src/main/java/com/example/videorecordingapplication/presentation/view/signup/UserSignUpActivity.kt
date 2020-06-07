package com.example.videorecordingapplication.presentation.view.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.videorecordingapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_signup)

        val viewPager : ViewPager2 = findViewById(R.id.pager_signup)
        viewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when(position){
                    1 -> TeacherSignUpFragment()
                    0 -> StudentSignUpFragment()
                    else -> TeacherSignUpFragment()
                }
            }
        }

        val tabLayout : TabLayout = findViewById(R.id.tab_signup)
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = when(position){
                    1 -> getString(R.string.text_btn_signup_teacher)
                    0 -> getString(R.string.text_btn_signup_student)
                    else -> getString(R.string.text_btn_signup_teacher)
                }
            }).attach()
    }
}
