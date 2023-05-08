package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
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
import org.json.JSONArray

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
        txt_createAccount.text = CustomFunctions().spanTextCustom(txt_createAccount.text.toString(),false,this,emptyArray(),arrayOf("Create one"),arrayOf("#FF8300"))

        // Click 'Login' Button
        btn_login.setOnClickListener {
            // Open pop-up window
            //popUpWindow()

            if (hasNetwork)
            {
                // Login
                var password = input_password.text.toString()
                var email = input_email.text.toString()
                NetworkFunctions().loginRequest(this, "https://boncafe-backend.herokuapp.com/login", ::goHomePage, password, email)
            }
        }

        // Click 'Create Account' TextView
        txt_createAccount.setOnClickListener {
            // Go to Sign Up page
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // Go to homepage after login
    fun goHomePage(context : Context, json : JSONArray){
        var jsonobj = json.getJSONObject(0)
        if (jsonobj.has("confirmed"))
        {
            startActivity(Intent(context, HomeActivity::class.java))
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