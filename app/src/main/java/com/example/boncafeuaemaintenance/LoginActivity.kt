package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var hasNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        // Check if has wifi or not
        var connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetwork ->
            hasNetwork = isNetwork

            var params = connection_error.layoutParams
            if (hasNetwork) params.height = 0
            else params.height = 80

            connection_error.layoutParams = params
        }

        // Set color to a specific TextView's text
        txt_createAccount.text = CustomFunctions().spanTextColor(txt_createAccount.text.toString(),12,txt_createAccount.text.toString().length,"#FF8300")

        // Click 'Login' Button
        btn_login.setOnClickListener {
            // Open pop-up window
            popUpWindow()
        }

        // Click 'Create Account' TextView
        txt_createAccount.setOnClickListener {
            // Go to Sign Up page
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // Pop-Up Window
    @SuppressLint("InflateParams")
    private fun popUpWindow(){
        // Referencing
        val popupBinding=layoutInflater.inflate(R.layout.window_verification,null)
        val backButton = popupBinding.findViewById<ImageView>(R.id.icon_btn_back)

        //Make pop-window as Dialog
        val myPopup= Dialog(this)
        myPopup.setContentView(popupBinding)

        //Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window
        backButton.setOnClickListener{
            myPopup.dismiss()
        }
    }
}