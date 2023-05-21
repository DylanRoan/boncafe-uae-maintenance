package com.example.boncafeuaemaintenance

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        startVideo()

        Timer().schedule(5)
        {
            var mainActivity = Intent(this@SplashScreen, OnBoardingScreen::class.java)
            startActivity(mainActivity)
        }
    }

    fun startVideo()
    {
        var uri = Uri.parse("android.resource://"
                + packageName
                + "/"
                + R.raw.coffee)
        videoview.setVideoURI(uri)

        videoview.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.setVolume(0f, 0f)
            val videoRatio = mediaPlayer.videoWidth / mediaPlayer.videoHeight.toFloat()
            val screenRatio = videoview.width / videoview.height.toFloat()
            val scaleX = videoRatio / screenRatio
            if (scaleX >= 1f) {
                videoview.scaleX = scaleX
            } else {
                videoview.scaleY = 1f / scaleX
            }
        }

        videoview.start()
    }
}