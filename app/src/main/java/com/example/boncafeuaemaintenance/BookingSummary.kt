package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_booking_summary.*
import org.json.JSONArray
import pl.droidsonroids.gif.GifImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingSummary.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingSummary : Fragment() {
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

    private val sharedViewModel: SharedViewModel by activityViewModels()

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking_summary, container, false)

        // References (for this layout)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)

        // Update Serial numbers
        val txtTotalCoffeeMachines = view.findViewById<TextView>(R.id.txt_totalCoffeeMachines)
        val txtSerialNumbers = view.findViewById<TextView>(R.id.txt_serialNumbers)

        sharedViewModel.concatenatedSerialIDs.observe(viewLifecycleOwner){text->
            txtSerialNumbers.text = text
        }

        sharedViewModel.coffeMachineCount.observe(viewLifecycleOwner){text->
            txtTotalCoffeeMachines.text = text
        }

        //update details
        val txtPhoneNumber = view.findViewById<TextView>(R.id.txt_contactNumber)
        val txtLocation = view.findViewById<TextView>(R.id.txt_location)
        val txtIssue = view.findViewById<TextView>(R.id.txt_issue)

        sharedViewModel.phoneNumberData.observe(viewLifecycleOwner){text ->
            txtPhoneNumber.text = text
        }

        sharedViewModel.locationData.observe(viewLifecycleOwner){text ->
            txtLocation.text = text
        }

        sharedViewModel.detailsData.observe(viewLifecycleOwner){text ->
            txtIssue.text = text
        }

        btnConfirm.setOnClickListener {
            popUpWindow(view.context)

            val prefName = "com.boncafe_maintenance.app"
            val prefs = view.context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)

            val email = prefs.getString("$prefName.email", "[]").toString()
            val password = prefs.getString("$prefName.password", "[]").toString()

            val name = prefs.getString("$prefName.name", "[NAME]").toString()


            var text = "$name is requesting maintenance for their ${txtTotalCoffeeMachines.text} coffee machines." +
                    "\n\nContact Information\nPhone number: ${txtPhoneNumber.text}" +
                    "\nLocation: ${txtLocation.text}" +
                    "\n\nSerial number(s):\n${txtSerialNumbers.text}" +
                    "\n\nDetails:\n${txtIssue.text}"

            var html = "<h2>$name is requesting maintenance for their ${txtTotalCoffeeMachines.text} coffee machines.</h2>" +
                    "<h3><b>Contact Information</b></h3><b>Phone number</b>: ${txtPhoneNumber.text}" +
                    "<br><b>Location</b>: ${txtLocation.text}" +
                    "<h3><b>Serial Number(s)</b></h3>${txtSerialNumbers.text.toString().replace("\n", "<br>")}" +
                    "<h3><b>Details</b></h3>${txtIssue.text}"

            NetworkFunctions().emailRequest(view.context, ::emailSent, password, email, text, html)
        }

        return view
    }

    fun emailSent(context : Context, json : JSONArray)
    {
        Log.d("BACKEND", json.toString())

        if (!json.getJSONObject(0).has("login")) {
            //TODO CONFIRM EMAIL SENT
        } //login failed
        else {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun popUpWindow(context: Context){
        // Referencing
        val popupBinding = layoutInflater.inflate(R.layout.window_confirmation,null)
        val backButton = popupBinding.findViewById<ImageView>(R.id.icon_btn_back)
        val popupHeader = popupBinding.findViewById<TextView>(R.id.txt_window_header)
        val popupText = popupBinding.findViewById<TextView>(R.id.txt_window_text)
        val btnYes = popupBinding.findViewById<Button>(R.id.btn_window_yes)
        val btnNo = popupBinding.findViewById<Button>(R.id.btn_window_no)
        val popupGif = popupBinding.findViewById<GifImageView>(R.id.gif_window)

        // Set Header and description
        popupHeader.text = "CONFIRMATION"
        popupText.text = "Are you sure you want to confirm?"

        popupGif.visibility = View.GONE
        backButton.visibility = View.GONE

        //Make pop-window as Dialog
        val myPopup= Dialog(context)
        myPopup.setContentView(popupBinding)

        //Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        btnYes.setOnClickListener {
            myPopup.dismiss()

            // TODO Code here for sending to email

            popUpWindowConfirmed(context)
        }

        btnNo.setOnClickListener {
            myPopup.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun popUpWindowConfirmed(context: Context){
        // Referencing
        val popupBinding = layoutInflater.inflate(R.layout.window_confirmation,null)
        val backButton = popupBinding.findViewById<ImageView>(R.id.icon_btn_back)
        val popupHeader = popupBinding.findViewById<TextView>(R.id.txt_window_header)
        val popupText = popupBinding.findViewById<TextView>(R.id.txt_window_text)
        val btnYes = popupBinding.findViewById<Button>(R.id.btn_window_yes)
        val btnNo = popupBinding.findViewById<Button>(R.id.btn_window_no)
        val btnLayout = popupBinding.findViewById<LinearLayout>(R.id.layout_window_buttons)
        val popupGif = popupBinding.findViewById<GifImageView>(R.id.gif_window)

        // Set Header and description
        popupHeader.text = "SUCCESS"
        popupText.text = "Your issue has been sent.\nPlease wait for us to reach out to you to set up an appointment."
        btnYes.text = "OK"

        btnNo.visibility = View.GONE
        backButton.visibility = View.GONE

        //Make pop-window as Dialog
        val myPopup= Dialog(context)
        myPopup.setContentView(popupBinding)

        //Display pop-up window
        myPopup.setCancelable(true)
        myPopup.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myPopup.show()

        btnYes.setOnClickListener {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }

        myPopup.setOnCancelListener {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingSummary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingSummary().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}