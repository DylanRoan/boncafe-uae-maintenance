package com.example.boncafeuaemaintenance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_on_boarding_screen.*

class OnBoardingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_screen)

        // Adapt ViewPager for onboard slides (as layouts)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager_onboardSlides)
        val layoutList = listOf(
            R.layout.layout_onboard_start,
            R.layout.layout_onboard_booking,
            R.layout.layout_onboard_report,
            R.layout.layout_onboard_maintenance)
        val adapter = CustomViewPagerAdapter(layoutList)
        viewPager.adapter = adapter

        slides_indicator.setViewPager(viewPager)
    }
}