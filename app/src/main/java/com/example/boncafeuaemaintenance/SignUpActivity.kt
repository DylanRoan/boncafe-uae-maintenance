package com.example.boncafeuaemaintenance

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_home.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Click 'Back' Button
        icon_btn_back.setOnClickListener {
            // Go to Sign Up page
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Referencing spannable substrings
        val textTerms = "By signing up, you agree to our Terms & Conditions and Privacy Policy"
        val textTermsStartIndexArr = arrayOf(
            "By signing up, you agree to our ".length,
            "By signing up, you agree to our Terms & Conditions and ".length
        )
        val textTermsEndIndexArr = arrayOf(
            "By signing up, you agree to our Terms & Conditions".length,
            textTerms.length
        )
        val textTermsColors = arrayOf("#FF8300","#FF8300")

        // Set colors to two substrings of TextView
        txt_terms.text = CustomFunctions().spanTextColor(textTerms, textTermsStartIndexArr, textTermsEndIndexArr, textTermsStartIndexArr.size, textTermsColors)
    }
}