package com.example.boncafeuaemaintenance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var hasNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        var connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetwork ->
            hasNetwork = isNetwork

            var params = connection_error.layoutParams
            if (hasNetwork) params.height = 0
            else params.height = 80

            connection_error.layoutParams = params
        }
    }
}