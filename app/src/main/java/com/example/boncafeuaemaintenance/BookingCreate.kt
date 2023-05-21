package com.example.boncafeuaemaintenance

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
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
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifDrawableInit
import pl.droidsonroids.gif.GifImageView

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

        // Back button
        icon_back.setOnClickListener {
            onBackPressed()
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        popUpWindow(this)
    }

    @SuppressLint("SetTextI18n")
    private fun popUpWindow(context: Context){
        // Referencing
        val popupBinding = layoutInflater.inflate(R.layout.window_confirmation,null)
        val backButton = popupBinding.findViewById<ImageView>(R.id.icon_btn_back)
        val popupHeader = popupBinding.findViewById<TextView>(R.id.txt_window_header)
        val popupText = popupBinding.findViewById<TextView>(R.id.txt_window_text)
        val btnYes = popupBinding.findViewById<Button>(R.id.btn_window_yes)
        val btnNo = popupBinding.findViewById<Button>(R.id.btn_window_no)
        val popupGif = popupBinding.findViewById<GifImageView>(R.id.gif_window)

        // Set Header and description
        popupHeader.text = "UNSAVED CHANGES"
        popupText.text = "Are you sure you want to go back?"

        popupGif.visibility = View.GONE
        backButton.visibility = View.GONE

        //Make pop-window as Dialog
        val myPopup= Dialog(context)
        myPopup.setContentView(popupBinding)

        //Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        btnYes.setOnClickListener {
            super.onBackPressed()
        }

        btnNo.setOnClickListener {
            myPopup.dismiss()
        }
    }
}