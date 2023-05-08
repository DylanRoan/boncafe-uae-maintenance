package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.fragment_maintenance.*
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

    // Do main coding here
    @SuppressLint("NewApi", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maintenance, container, false)

        // Update Last Maintenance date
        SetDate().updateLastMaintenanceDate(view)

        // Update Contract date ending
        SetDate().updateContractDaysLeft(view)

        // Display user's owned coffee machines
        val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)
        createOwnedCoffeeMachines(mainLayout)

        return view
    }

    // Create user's owned coffee machines
    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun createOwnedCoffeeMachines(mainLayout: LinearLayout){
        // JSON Placeholder
        val coffeeMachineDetails = mapOf(
            "Coffee Machine 1" to mapOf(
                "Model No" to "834923",
                "Serial No" to "EZDDBXSO",
                "Purchased date" to "2022-01-01"
            ),
            "Coffee Machine 2" to mapOf(
                "Model No" to "654234",
                "Serial No" to "KWNDJFNE",
                "Purchased date" to "2022-05-03"
            ),
            "Coffee Machine 3" to mapOf(
                "Model No" to "133742",
                "Serial No" to "QWERTYU",
                "Purchased date" to "2022-09-06"
            ),
        )

        // Update info
        for (i in coffeeMachineDetails){
            val layoutCoffeeMachine = layoutInflater.inflate(R.layout.container_coffee_machine, null, false)
            val txtCoffeeMachineName = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_coffeeMachineName)
            val txtModelNo = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_modelNo)
            val txtSerial = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_serialNo)
            val txtPurchased = layoutCoffeeMachine.findViewById<TextView>(R.id.txt_purchasedDate)

            txtCoffeeMachineName.text = i.key
            txtModelNo.text = i.value["Model No"]
            txtSerial.text = i.value["Serial No"]
            txtPurchased.text = i.value["Purchased date"]

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