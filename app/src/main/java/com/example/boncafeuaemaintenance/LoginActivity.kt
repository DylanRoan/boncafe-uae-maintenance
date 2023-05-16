package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray

class LoginActivity : AppCompatActivity() {

    var hasNetwork = false

     //insecure at the moment, will be changed
    var email = ""
    var password = ""

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
            if (hasNetwork)
            {
                // Login
                password = input_password.text.toString()
                email = input_email.text.toString()
                NetworkFunctions().loginRequest(this, "https://boncafe-backend.herokuapp.com/login", ::getInfo, password, email)

            } else {
                // Open pop-up window
                popUpWindow()
            }
        }

        // Click 'Create Account' TextView
        txt_createAccount.setOnClickListener {
            // Go to Sign Up page
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    //save to saved preferences
    val prefName = "com.boncafe_maintenance.app"

    // Get info after login
    fun getInfo(context : Context, json : JSONArray){
        val jsonobj = json.getJSONObject(0)
        if (jsonobj.has("confirmed"))
        {
            val prefs = context.getSharedPreferences(prefName, MODE_PRIVATE)
            prefs.edit().putString("$prefName.email", email).apply() //insecure
            prefs.edit().putString("$prefName.password", password).apply() //insecure

            //check if remember me is true
            if (check_remember_me.isChecked) {
                //not required at the moment
                prefs.edit().putBoolean("$prefName.remember_me", true).apply() //insecure
            }

            //get products
            NetworkFunctions().loginRequest(context, "https://boncafe-backend.herokuapp.com/table", ::getContract, password, email)


        }
        else {
            //FAILED LOGIN MESSAGE : TODO
        }
    }

    //get contract after getting products
    fun getContract(context : Context, json : JSONArray){
        val jsonobj = json.getJSONObject(0)
        if (!jsonobj.has("confirmed"))
        {
            val prefs = context.getSharedPreferences(prefName, MODE_PRIVATE)
            prefs.edit().putString("$prefName.products", json.toString()).apply()
        }

        Log.i("BACKEND : PRODUCT DB", json.toString()) // DEBUG TODO REMOVE

        //go to home page after getting contract
        NetworkFunctions().loginRequest(context, "https://boncafe-backend.herokuapp.com/contract", ::goToHome, password, email)
    }

    //go to home page after receiving contract
    fun goToHome(context : Context, json : JSONArray) {
        Log.i("BACKEND : CONTRACT DB", json.toString()) // DEBUG TODO REMOVE

        if(json.length() > 0)
        {
            val prefs = context.getSharedPreferences(prefName, MODE_PRIVATE)
            prefs.edit().putString("$prefName.contract", json.toString()).apply()
        }

        //go home
        startActivity(Intent(context, HomeActivity::class.java))
    }

    // Pop-Up Window
    @SuppressLint("InflateParams")
    private fun popUpWindow(){
        // Referencing
        val popupBinding=layoutInflater.inflate(R.layout.window_unverified,null)
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