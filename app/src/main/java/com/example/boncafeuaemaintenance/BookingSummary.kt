package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_booking_summary.*
import org.json.JSONArray

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
        if (json.getJSONObject(0).has("login")) {} //login failed
        Log.d("BACKEND", json.toString())

        //TODO CONFIRM EMAIL SENT
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