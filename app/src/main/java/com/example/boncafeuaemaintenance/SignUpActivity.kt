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

        // Create SpannableString
        val textTerms = "By signing up, you agree to our Terms & Conditions and Privacy Policy"
        val ss = SpannableString(textTerms)

        // Span first text
        val textTermsFirstIndex = "By signing up, you agree to our ".length
        val textTermsEndIndex = "By signing up, you agree to our Terms & Conditions".length

        // Span second text
        val textPrivacyFirstIndex = "By signing up, you agree to our Terms & Conditions and ".length
        val textPrivacyEndIndex = textTerms.length

        // Set color to a specific TextView's text
        ss.setSpan(ForegroundColorSpan(Color.parseColor("#FF8300")), textTermsFirstIndex,textTermsEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(ForegroundColorSpan(Color.parseColor("#FF8300")), textPrivacyFirstIndex,textPrivacyEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_terms.text = ss
    }
}