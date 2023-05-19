package com.example.boncafeuaemaintenance

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_home.*

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Click 'Back' Button
        icon_btn_back.setOnClickListener {
            super.onBackPressed()
        }

        // Create account button
        btn_createAccount.setOnClickListener {
            // Pop up verification window
            popUpWindow()
        }

        // Set colors and clickable to TextView's substrings
        val textTerms = "By signing up, you agree to our Terms & Conditions and Privacy Policy"
        val textTermsSpans = arrayOf("Terms & Conditions", "Privacy Policy")
        val textTermsURL = arrayOf("https://www.boncafeme.ae/page/terms-conditions", "https://www.boncafeme.ae/page/privacy-policy")
        val textTermsColors = arrayOf("#FF8300","#FF8300")
        txt_terms.text = CustomFunctions().spanTextCustom(textTerms,true,this,textTermsURL,textTermsSpans,textTermsColors)
        txt_terms.movementMethod = LinkMovementMethod.getInstance()
    }

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