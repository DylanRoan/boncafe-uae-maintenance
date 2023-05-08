package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_home.*

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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        // References
        val linearLayoutRecent = view.findViewById<LinearLayout>(R.id.linearLayout_recent)

        // References (For Notification Layout)
        val customLayoutNotification = inflater.inflate(R.layout.layout_notification, container, false)
        val txtNotifDescription = customLayoutNotification.findViewById<TextView>(R.id.txt_notif_description)
        val txtNotifTitle = customLayoutNotification.findViewById<TextView>(R.id.txt_notif_title)
        val linearLayoutNotificationContainer = customLayoutNotification.findViewById<LinearLayout>(R.id.linearLayout_notificationContainer)

        // Get days left of contract expiration
        val contractDaysLeft = SetDate().getDaysLeft(SetDate().contractEndDate)
        val yellowColorHex = "#F29D38"
        val redColorHex = "#FF0000"

        // Update and display Contract Expiration Notification
        if (contractDaysLeft == 0) updateContractExpirationNotification(view,true,customLayoutNotification,linearLayoutRecent,contractDaysLeft,txtNotifDescription,txtNotifTitle,linearLayoutNotificationContainer,redColorHex)
        else if (contractDaysLeft <= 7) updateContractExpirationNotification(view,false,customLayoutNotification,linearLayoutRecent,contractDaysLeft,txtNotifDescription,txtNotifTitle,linearLayoutNotificationContainer,redColorHex)
        else if (contractDaysLeft <= 31) updateContractExpirationNotification(view,false,customLayoutNotification,linearLayoutRecent,contractDaysLeft,txtNotifDescription,txtNotifTitle,linearLayoutNotificationContainer,yellowColorHex)

        // Remove Contract Expiration Notification
        if (contractDaysLeft < 0 || contractDaysLeft > 31) linearLayoutRecent.removeView(customLayoutNotification)

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun updateContractExpirationNotification(view: View, expired: Boolean, customLayoutNotification: View, linearLayoutRecent: LinearLayout, contractDaysLeft: Int, txtNotifDescription: TextView, txtNotifTitle: TextView, linearLayoutNotificationContainer: LinearLayout, colorHex: String){
        if (!expired){
            // Highlight 'X days left'
            val txtContractDesc="Your contract ends in $contractDaysLeft days. Awaiting for your renewal."
            val txtContractDescSpan=arrayOf("$contractDaysLeft days")
            txtNotifDescription.text = CustomFunctions().spanTextCustom(txtContractDesc,false,view.context,
                emptyArray(),txtContractDescSpan,arrayOf(colorHex))
        } else {
            // Contract expired
            val txtContractDesc="Your contract has expired. Please contact our Technical Support if you wish to renew one."
            val txtContractDescSpan=arrayOf("Technical Support")
            txtNotifDescription.text = CustomFunctions().spanTextCustom(txtContractDesc,false,view.context,
                emptyArray(),txtContractDescSpan,arrayOf("#FF8300"))

            // Change header text
            txtNotifTitle.text = "Contract Expired"
        }

        // Change notification's header color
        txtNotifTitle.setTextColor(Color.parseColor(colorHex))

        // Change notification's border color
        val drawable = linearLayoutNotificationContainer.background as GradientDrawable
        drawable.setStroke(7,Color.parseColor(colorHex))
        
        // Create and display notification
        linearLayoutRecent.addView(customLayoutNotification)
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