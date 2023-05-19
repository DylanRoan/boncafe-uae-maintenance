package com.example.boncafeuaemaintenance

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    var email = ""
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Click 'Back' Button
        icon_btn_back.setOnClickListener {
            super.onBackPressed()
        }

        // Create account button
        btn_createAccount.setOnClickListener {

            //checks if all forms are filled
            if (input_fullName.text.toString().isNotEmpty() &&
                    input_email.text.toString().isNotEmpty() &&
                    input_password.text.toString().isNotEmpty() &&
                    input_confirmPass.text.toString().isNotEmpty())
            {
                txt_warning_enterBlankFields.visibility = View.GONE

                //confirm matching passwords
                if (input_password.text.toString() == input_confirmPass.text.toString())
                {
                    txt_warning_password.visibility = View.GONE

                    email = input_email.text.toString()
                    password = input_password.text.toString()

                    NetworkFunctions().signupRequest(this, "https://boncafe-backend.herokuapp.com/adduser",
                        ::sendConfirmation,
                        password, email, input_fullName.text.toString())
                }
                else
                {
                    txt_warning_password.visibility = View.VISIBLE
                }
            }
            else
            {
                txt_warning_enterBlankFields.visibility = View.VISIBLE
            }
        }

        // Set colors and clickable to TextView's substrings
        val textTerms = "By signing up, you agree to our Terms & Conditions and Privacy Policy"
        val textTermsSpans = arrayOf("Terms & Conditions", "Privacy Policy")
        val textTermsURL = arrayOf("https://www.boncafeme.ae/page/terms-conditions", "https://www.boncafeme.ae/page/privacy-policy")
        val textTermsColors = arrayOf("#FF8300","#FF8300")
        txt_terms.text = CustomFunctions().spanTextCustom(textTerms,true,this,textTermsURL,textTermsSpans,textTermsColors)
        txt_terms.movementMethod = LinkMovementMethod.getInstance()
    }

    fun sendConfirmation(context : Context, jsonArray: JSONArray)
    {
        Log.i("BACKEND : SIGNUP", jsonArray.toString())

        NetworkFunctions().loginRequest(context, "https://boncafe-backend.herokuapp.com/emailconfirm", ::popUpWindow, password, email)
    }

    fun popUpWindow(context : Context, jsonArray: JSONArray)
    {
        Log.i("BACKEND : SIGNUP", jsonArray.toString())

        val jsonObj = jsonArray.getJSONObject(0)
        if (jsonObj.has("message"))
        {
            runOnUiThread {
                txt_warning_invalidEmail.visibility = View.VISIBLE
            }
        }
        else {
            runOnUiThread {
                txt_warning_invalidEmail.visibility = View.GONE

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
        }
    }
}