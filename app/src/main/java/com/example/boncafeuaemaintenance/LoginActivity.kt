package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
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
        txt_createAccount.text = spanTextColor(txt_createAccount.text.toString(),12,txt_createAccount.text.toString().length,"#FF8300")

        // Click Login Button
        btn_login.setOnClickListener {
            // Open pop-up window
            popUpWindow()
        }
    }

    /** To set a specific color of a substring
     * @param _text Pass a text (as String)
     * @param startIndexValue Start position of a substring to be coloured (as Int)
     * @param endIndexValue End position of a substring to be coloured (as Int)
     * @param colorInHex Pass colour hex code (as String)
     * @return substring with its set colour (as SpannableString) **/
    fun spanTextColor(_text: String, startIndexValue: Int, endIndexValue: Int, colorInHex: String): SpannableString {
        val spannableString = SpannableString(_text)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(colorInHex)),
            startIndexValue,
            endIndexValue,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
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