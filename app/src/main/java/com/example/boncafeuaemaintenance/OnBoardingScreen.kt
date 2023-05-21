package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_booking_create.*
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

        // Set up indicator for ViewPager
        slides_indicator.setViewPager(viewPager)

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {

                if (position==layoutList.lastIndex){
                    btn_getStarted.visibility = View.VISIBLE
                } else {
                    btn_getStarted.visibility = View.INVISIBLE
                }

                if (position==0){
                    layout_swipe.visibility = View.VISIBLE
                } else {
                    layout_swipe.visibility = View.GONE
                }
            }
        })

        txt_skip.setOnClickListener {
            this.startActivity(Intent(this,LoginActivity::class.java))
        }

        btn_getStarted.setOnClickListener {
            this.startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    override fun onBackPressed() {
        // nothing
    }
}