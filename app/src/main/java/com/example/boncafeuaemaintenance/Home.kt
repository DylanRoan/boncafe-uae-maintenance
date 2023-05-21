package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
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

    // Initialisation
    private var contractDaysLeft: Int = 0

    // Notification layout references
    private lateinit var linearLayoutRecent: LinearLayout
    private lateinit var customLayoutNotification: ViewGroup
    private lateinit var linearLayoutRecentContainer: LinearLayout
    private lateinit var imgNotif: ImageView
    private lateinit var txtNotifDescription: TextView
    private lateinit var txtNotifTitle: TextView

    // Recent layout
    private lateinit var noRecentNotificationLayout: RelativeLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        // References
        linearLayoutRecent = view.findViewById(R.id.linearLayout_recent)
        noRecentNotificationLayout = view.findViewById(R.id.layout_noRecent)
        val viewFlipperPromotions = view.findViewById<ViewFlipper>(R.id.viewFlipper_promotions)
        val btnBookNow = view.findViewById<Button>(R.id.btn_book_now)

        // Check for contract expiration notification
        val prefName = "com.boncafe_maintenance.app"
        val prefs = view.context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)

        val email = prefs.getString("$prefName.email", "none") as String
        val password = prefs.getString("$prefName.password", "none") as String

        //contract network call
        NetworkFunctions().maintenancePageRequest(view, view.context,
            "https://boncafe-backend.herokuapp.com/contract",
            ::displayContractExpiration, password, email
        )

        // Clickable promotions
        viewFlipperPromotions.setOnClickListener {
            CustomFunctions().openURL(view.context,"https://www.boncafeme.ae/shop?category=machine-deals&&%20category_name=Machine%20Deals")
        }

        // Book Now Button
        btnBookNow.setOnClickListener {
            view.context.startActivity(Intent(view.context, BookingCreate::class.java))
        }

        return view
    }

    private fun displayContractExpiration(view: View, context: Context, jsonArray: JSONArray){
        if (jsonArray.length() <= 0) return

        if (!jsonArray.getJSONObject(0).has("login")) //if login did not fail
        {
            val contract = jsonArray.getJSONObject(0)

            activity?.runOnUiThread {
                // References (For Notification Layout)
                customLayoutNotification = layoutInflater.inflate(R.layout.layout_notification, null, false) as ViewGroup
                txtNotifDescription = customLayoutNotification.findViewById(R.id.txt_notif_description)
                txtNotifTitle = customLayoutNotification.findViewById(R.id.txt_notif_title)
                imgNotif = customLayoutNotification.findViewById(R.id.img_notif)
                linearLayoutRecentContainer = customLayoutNotification.findViewById(R.id.linearLayout_notificationContainer)

                // Get days left of contract expiration
                contractDaysLeft = SetDate().getDaysLeft(contract.getString("end_date"))
                val yellowColorHex = "#F29D38"
                val redColorHex = "#FF0000"

                // Update and display Contract Expiration Notification (always display it at top)
                if (contractDaysLeft == 0) updateContractExpirationNotification(view,true,redColorHex)
                else if (contractDaysLeft <= 7) updateContractExpirationNotification(view,false,redColorHex)
                else if (contractDaysLeft <= 31) updateContractExpirationNotification(view,false,yellowColorHex)

                // Remove Contract Expiration Notification
                if (contractDaysLeft < 0 || contractDaysLeft > 31) linearLayoutRecent.removeView(customLayoutNotification)

                // Check empty notifications
                checkEmptyNotifcations(linearLayoutRecent,noRecentNotificationLayout)
            }
        }
        else
        {
            //login failed
            //TODO GO BACK TO LOGIN PAGE TEST
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateContractExpirationNotification(view: View, expired: Boolean, colorHex: String){
        if (!expired){
            // Highlight 'X days left'
            val txtContractDesc="Your contract ends in $contractDaysLeft days. Awaiting for your renewal."
            val txtContractDescSpan=arrayOf("$contractDaysLeft days")
            txtNotifDescription.text = CustomFunctions().spanTextCustom(txtContractDesc,false,view.context,
                emptyArray(),txtContractDescSpan,arrayOf(colorHex))
        } else {
            // Contract expired
            val txtContractDesc="Your contract has expired. Please contact us if you wish to renew one."
            val txtContractDescSpan=arrayOf("contact us")
            txtNotifDescription.text = CustomFunctions().spanTextCustom(txtContractDesc,false,view.context,
                emptyArray(),txtContractDescSpan,arrayOf("#FF8300"))

            // Change header text
            txtNotifTitle.text = "Contract Expired"
        }

        // Change notification's header color
        txtNotifTitle.setTextColor(Color.parseColor(colorHex))

        // Change notification's border color
        val drawable = linearLayoutRecentContainer.background as GradientDrawable
        drawable.setStroke(7,Color.parseColor(colorHex))

        // Set image
        imgNotif.setImageResource(R.drawable.img_contract)
        
        // Create and display notification
        linearLayoutRecent.addView(customLayoutNotification, 0)

        // Handle clicks
        customLayoutNotification.setOnClickListener{
            // Get a reference to the BottomNavigationView
            val homeActivity = requireActivity()
            val bottomNavigationView = homeActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

            // Go to maintenance page
            bottomNavigationView.selectedItemId = R.id.maintenance
        }
    }

    private fun checkEmptyNotifcations(linearLayoutRecent: LinearLayout, noRecentNotificationLayout: RelativeLayout){
        // Check if notifications are empty or not
        if (linearLayoutRecent.childCount == 0){
            // Display 'No recent notifications' illustration
            noRecentNotificationLayout.visibility = View.VISIBLE
        } else {
            noRecentNotificationLayout.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}