package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_maintenance.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Maintenance.newInstance] factory method to
 * create an instance of this fragment.
 */
class Maintenance : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var hasContract = true

    // Do main coding here
    @SuppressLint("NewApi", "SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maintenance, container, false)

        // View references
        val contractPeriodLayout = view.findViewById<LinearLayout>(R.id.AMC_period_container)
        val lastMaintenanceLayout = view.findViewById<LinearLayout>(R.id.lastMaintenance_container)

        //get preference
        val prefName = "com.boncafe_maintenance.app"
        val prefs = view.context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)

        val productsString = prefs.getString("$prefName.products", "[]").toString()
        val contractString = prefs.getString("$prefName.contract", "[]").toString()

        //change to json object / array
        val products = JSONArray(productsString)
        val contract = if (JSONArray(contractString).length() > 0) JSONArray(contractString).getJSONObject(0) else JSONObject("{'contract': 'none'}")

        Log.i("BACKEND : PRODUCT PREF", products.toString()) // DEBUG TODO REMOVE
        Log.i("BACKEND : CONTRACT PREF", contract.toString()) // DEBUG TODO REMOVE

        // Check if user has contract
        if (!contract.has("contract")){
            // Update maintenance due date
            SetDate().updateLastMaintenanceDate(view, contract.getString("maintenance"))

            // Update contract ending date
            SetDate().updateContractDaysLeft(view, contract.getString("start_date"),contract.getString("end_date"))

            // Set maintenance and contract layout visibility
            contractPeriodLayout.visibility = View.VISIBLE
            lastMaintenanceLayout.visibility = View.VISIBLE
        } else {
            // Set maintenance and contract layout visibility
            contractPeriodLayout.visibility = View.GONE
            lastMaintenanceLayout.visibility = View.GONE
        }

        // Display user's owned coffee machines
        val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)
        createOwnedCoffeeMachines(mainLayout, products)

        return view
    }


    // Create user's owned coffee machines
    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun createOwnedCoffeeMachines(mainLayout: LinearLayout, products : JSONArray){
        // Update info
        for (i in 0 until products.length()) {
            val layoutCoffeeMachine = layoutInflater.inflate(R.layout.container_coffee_machine, null, false)
            val txtCoffeeMachineName = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_coffeeMachineName)
            val imgCoffeeMachine = layoutCoffeeMachine.findViewById<ImageView>(R.id.img_coffeeMachine)
            val txtModelNo = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_modelNo)
            val txtSerial = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_serialNo)
            val txtPurchased = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_purchasedDate)

            val product = products.getJSONObject(i)

            txtCoffeeMachineName.text = product.getString("machine")
            Picasso.get().load("https://i.imgur.com/o8k9oRs.png").into(imgCoffeeMachine) // TODO CHANGE IMAGES LATER
            txtModelNo.text = product.getString("model")
            txtSerial.text = product.getString("serial")
            txtPurchased.text = SetDate().formatDateConvert(product.getString("maintenance_due")) // TODO CHANGE DATE / DELETE LATER

            mainLayout.addView(layoutCoffeeMachine)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Maintenance.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Maintenance().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}