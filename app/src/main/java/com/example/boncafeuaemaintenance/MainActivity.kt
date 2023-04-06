package com.example.boncafeuaemaintenance

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    var hasNetwork = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        var connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetwork ->
            hasNetwork = isNetwork

            var params = connection_error.layoutParams
            if (hasNetwork) params.height = 0
            else params.height = 80

            connection_error.layoutParams = params
        }

        button.setOnClickListener {
            //NetworkFunctions().loginRequest(this, "https://boncafe-backend.herokuapp.com/login", ::testFunction, "wah????", "omar@sunpoint.com")
            //Log.i("Connectivity", NetworkFunctions().isOnline(this).toString())
            startActivity(Intent(this, SplashScreen::class.java))
            if (hasNetwork){}


        }

        // Go to home page
        button2.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    fun testFunction(context : Context, json : JSONArray) {
        Log.i("Wah", json.toString())
        startActivity(Intent(context, SplashScreen::class.java))
    }

}