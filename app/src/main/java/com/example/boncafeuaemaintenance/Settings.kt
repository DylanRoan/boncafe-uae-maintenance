package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class Settings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // References
        val tabOfficeLocation = view.findViewById<ConstraintLayout>(R.id.tab_office_location)
        val tabWarehouseLocation = view.findViewById<ConstraintLayout>(R.id.tab_warehouse_location)
        val tabTerms = view.findViewById<ConstraintLayout>(R.id.tab_termsconditions)
        val tabPrivacyPolicy = view.findViewById<ConstraintLayout>(R.id.tab_privacy)
        val tabLogout = view.findViewById<ConstraintLayout>(R.id.tab_logout)

        // URLs
        val urlOfficeLocation = "https://goo.gl/maps/pciQUq5mKUrhrABd8"
        val urlWarehouseLocation = "https://goo.gl/maps/W5s1cT4D4x3AGAtt9"
        val urlTerms = "https://www.boncafeme.ae/page/terms-conditions"
        val urlPrivacyPolicy = "https://www.boncafeme.ae/page/privacy-policy"

        // Store Tabs and URLs
        val tabArrayWithLink = arrayOf(
            arrayOf(tabOfficeLocation,urlOfficeLocation),
            arrayOf(tabWarehouseLocation,urlWarehouseLocation),
            arrayOf(tabTerms,urlTerms),
            arrayOf(tabPrivacyPolicy,urlPrivacyPolicy)
        )

        // Make each tab open to their respective link
        for (tab in tabArrayWithLink){
            val targetConstLayout = tab[0] as ConstraintLayout
            val targetURL = tab[1] as String

            targetConstLayout.setOnClickListener{
                CustomFunctions().openURL(view.context, targetURL)
            }
        }

        // Logout button
        tabLogout.setOnClickListener {
            val prefName = "com.boncafe_maintenance.app"
            val prefs = view.context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
            prefs.edit().putString("$prefName.email", "none").apply() //insecure
            prefs.edit().putString("$prefName.password", "none").apply() //insecure
            prefs.edit().putBoolean("$prefName.remember_me", false).apply() //insecure
            startActivity(Intent(view.context, LoginActivity::class.java))
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Settings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Settings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}