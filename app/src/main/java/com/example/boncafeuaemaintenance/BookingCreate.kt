package com.example.boncafeuaemaintenance

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.ClipDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_booking_create.*
import kotlinx.android.synthetic.main.layout_booking_coffeemachines.view.*

class BookingCreate : AppCompatActivity(), LifecycleOwner {

    private val sharedViewModel: SharedViewModel by viewModels()

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_create)

        // Adapt ViewPager for fragments
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val fragmentList = listOf(
            BookingCoffeeMachines(),
            BookingContactProblem(),
            Booking_Photos(),
            BookingSummary())
        val adapter = FragmentViewPageAdapter(fragmentList, this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false // disable swipe gesture

        // For checking current page/fragment
        val pageTitleArray = arrayOf("Coffee Machines","More Details","Photos","Summary")
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                for (i in pageTitleArray.indices){
                    if (i==position){
                        // Change title to indicate current page
                        txt_bookingPageTitle.text = pageTitleArray[i]
                    }
                }
            }
        })

        // Animate Progress Bar initialisations
        val animateDuration: Long = 500
        val animateInitPoint = 1270
        val animatePoint1 = 4160
        val animatePoint2 = 7070
        val animatePoints = arrayOf(arrayOf(animateInitPoint,animateInitPoint), arrayOf(animateInitPoint,animatePoint1), arrayOf(animatePoint1,animatePoint2), arrayOf(animatePoint2,10000))
        var animatePointsCurrentIndex = 0

        // Init reset progress bar
        animateProgressBar(0,animateInitPoint,animateInitPoint)

        // For setting button visibility
        fun checkButtonVisibility(){
            when (animatePointsCurrentIndex) {
                0 -> {
                    btn_next_bookingPage.visibility = View.VISIBLE
                    btn_prev_bookingPage.visibility = View.INVISIBLE
                }
                pageTitleArray.size - 1 -> {
                    btn_next_bookingPage.visibility = View.INVISIBLE
                    btn_prev_bookingPage.visibility = View.VISIBLE
                }
                else -> {
                    btn_next_bookingPage.visibility = View.VISIBLE
                    btn_prev_bookingPage.visibility = View.VISIBLE
                }
            }
        }

        // Button for next page
        btn_next_bookingPage.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)

            // Increase animate bar
            if (animatePointsCurrentIndex<animatePoints.size-1){
                animatePointsCurrentIndex++
                animateProgressBar(animateDuration,animatePoints[animatePointsCurrentIndex][0],animatePoints[animatePointsCurrentIndex][1])
            }

            checkButtonVisibility()
        }

        // Button for previous page
        btn_prev_bookingPage.setOnClickListener {
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)

            // Decrease animate bar
            if (animatePointsCurrentIndex>0){
                animateProgressBar(animateDuration,animatePoints[animatePointsCurrentIndex][1],animatePoints[animatePointsCurrentIndex][0])
                animatePointsCurrentIndex--
            }

            checkButtonVisibility()
        }
    }

    // Create/Initialise single instance of ObjectAnimator (to avoid lag issues)
    private val widthAnimator: ObjectAnimator by lazy {
        // Make ImageView as clip drawable
        val drawable = img_progress_complete.drawable
        val clipDrawable = ClipDrawable(drawable, GravityCompat.START, ClipDrawable.HORIZONTAL)
        img_progress_complete.setImageDrawable(clipDrawable)

        // Create and configure the widthAnimator
        val animator = ObjectAnimator.ofInt(clipDrawable, "level", 0, 0)
        //animator.interpolator = DecelerateInterpolator()
        animator
    }

    // For animating progress bar by cropping its image
    // 0 to 10000 (min and max of progress bar)
    private fun animateProgressBar(duration: Long, startPoint: Int, endPoint: Int) {
        // Start the widthAnimator animation
        widthAnimator.setIntValues(startPoint, endPoint)
        widthAnimator.duration = duration
        widthAnimator.start()
    }
}