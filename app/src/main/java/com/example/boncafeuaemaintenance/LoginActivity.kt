package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray

class LoginActivity : AppCompatActivity() {

    var hasNetwork = false

     //insecure at the moment, will be changed
    var email = ""
    var password = ""

    //save to saved preferences
    val prefName = "com.boncafe_maintenance.app"
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

            if (hasNetwork) rememberMe()
        }

        // Set color to a specific TextView's text
        txt_createAccount.text = CustomFunctions().spanTextCustom(txt_createAccount.text.toString(),false,this,emptyArray(),arrayOf("Create one"),arrayOf("#FF8300"))

        // Click 'Login' Button
        btn_login.setOnClickListener {
            if (hasNetwork)
            {
                // Login
                password = input_password.text.toString()
                email = input_email.text.toString()
                NetworkFunctions().loginRequest(this, "https://boncafe-backend.herokuapp.com/login", ::login, password, email)

            } else {
                // NO WIFI MESSAGE CUZ YOURE TRYNA LOGIN FOR SOME REASON
                Toast.makeText(this, "No connection.", Toast.LENGTH_SHORT).show()
            }
        }

        // Click 'Create Account' TextView
        txt_createAccount.setOnClickListener {
            // Go to Sign Up page
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    fun rememberMe()
    {
        //check if remember me is true
        val prefs = getSharedPreferences(prefName, MODE_PRIVATE)
        var rememberMe = prefs.getBoolean("$prefName.remember_me", false)
        Log.i("BACKEND", "REMEMBER ME $rememberMe")
        if (rememberMe)
        {
            email = prefs.getString("$prefName.email", "none") as String
            password = prefs.getString("$prefName.password", "none") as String

            if (email != "none" && password != "none")
            {
                if (hasNetwork)
                {
                    // Login
                    Log.i("BACKEND", "LOGGING IN")
                    NetworkFunctions().loginRequest(this, "https://boncafe-backend.herokuapp.com/login", ::login, password, email)
                }
            }
        }
    }

    // Get info after login
    fun login(context : Context, json : JSONArray){
        val jsonobj = json.getJSONObject(0)

        Log.i("BACKEND", "LOGIN: $jsonobj")
        val prefs = context.getSharedPreferences(prefName, MODE_PRIVATE)

        if (!jsonobj.has("login"))
        {
            prefs.edit().putString("$prefName.email", email).apply() //insecure
            prefs.edit().putString("$prefName.password", password).apply() //insecure
            prefs.edit().putString("$prefName.name", jsonobj.getString("name")).apply() //insecure

            //check if remember me is true
            if (check_remember_me.isChecked) {
                prefs.edit().putBoolean("$prefName.remember_me", true).apply() //insecure
            }

            if (jsonobj.get("confirmed") as Boolean)
            {
                //go home
                startActivity(Intent(context, HomeActivity::class.java))
            }
            else
            {
                runOnUiThread { popUpWindow(context) }
                prefs.edit().putBoolean("$prefName.remember_me", false).apply() //insecure
            }

        }
        else {
            //FAILED LOGIN MESSAGE : TODO
            runOnUiThread {
                val failedLogin = findViewById<TextView>(R.id.txt_warning_incorrect)
                failedLogin.visibility = View.VISIBLE
            }

            prefs.edit().putString("$prefName.email", "none").apply() //insecure
            prefs.edit().putString("$prefName.password", "none").apply() //insecure
            prefs.edit().putBoolean("$prefName.remember_me", false).apply() //insecure
        }
    }

    // Pop-Up Window
    @SuppressLint("InflateParams")
    private fun popUpWindow(context: Context){
        // Referencing
        val popupBinding=layoutInflater.inflate(R.layout.window_verification,null)
        val backButton = popupBinding.findViewById<ImageView>(R.id.icon_btn_back)

        //Make pop-window as Dialog
        val myPopup= Dialog(context)
        myPopup.setContentView(popupBinding)

        //Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        // Close pop-up window
        backButton.setOnClickListener{
            myPopup.dismiss()
            context.startActivity(Intent(context, LoginActivity::class.java))
        }

        myPopup.setOnCancelListener {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onBackPressed() {
        // nothing (for disabling back button)
    }
}