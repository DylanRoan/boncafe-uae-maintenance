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

        // Last Maintenance date
        val lastMaintenanceDate = "2023-03-11"

        // Start date and End date of Contract
        val contractStartDate = "2023-01-01"
        val contractEndDate = "2023-12-31"

        // Update last maintenance date
        updateLastMaintenanceDate(view, lastMaintenanceDate)

        // Update contract date ending
        updateContractDaysLeft(view, contractStartDate, contractEndDate)

        // Inflate the layout for this fragment
        return view
    }

    /**
     * Calculate number of days ago
     * @param date Pass a date as "yyyy-MM-dd" (as String)
     * @return number of days ago (as Int)
     * */
    @SuppressLint("NewApi")
    fun getDaysAgo(date: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val pastDate = LocalDate.parse(date, formatter)
        val currentDate = LocalDate.now()
        val daysAgo = currentDate.toEpochDay() - pastDate.toEpochDay()

        return daysAgo.toInt()
    }

    /**
     * Calculate number of days left
     * @param date Pass a date as "yyyy-MM-dd" (as String)
     * @return number of days left (as Int)
     * */
    @SuppressLint("NewApi")
    fun getDaysLeft(date: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val futureDate = LocalDate.parse(date, formatter)
        val currentDate = LocalDate.now()
        val daysLeft = futureDate.toEpochDay() - currentDate.toEpochDay()

        return daysLeft.toInt()
    }

    // Update last maintenance date
    @SuppressLint("SetTextI18n")
    private fun updateLastMaintenanceDate(view: View, lastMaintenanceDate: String){
        val daysAgo = getDaysAgo(lastMaintenanceDate)

        // Date Conversion
        val monthsAgo = daysAgo / 30
        val yearsAgo = monthsAgo / 12

        // Update text (number of days ago)
        val txtDateMaintenance = view.findViewById<TextView>(R.id.txt_date_maintenance)
        if (daysAgo == 0) txtDateMaintenance.text = "Today"
        else if (daysAgo < 31) txtDateMaintenance.text = "$daysAgo Days Ago"
        else if (daysAgo < 365) txtDateMaintenance.text = "$monthsAgo Months Ago"
        else txtDateMaintenance.text = "$yearsAgo Years Ago"

        // Update text color (based on: below 3 months, below 6 months, more than 6 months )
        if (daysAgo < 90) txtDateMaintenance.setTextColor(Color.parseColor("#009B06"))
        else if (daysAgo < 180) txtDateMaintenance.setTextColor(Color.parseColor("#F29D38"))
        else txtDateMaintenance.setTextColor(Color.parseColor("#FF0000"))
    }

    // Update contract date ending
    @SuppressLint("SetTextI18n")
    private fun updateContractDaysLeft(view: View, contractStartDate: String, contractEndDate: String){
        val daysLeft = getDaysLeft(contractEndDate)

        // Update text (number of days left and contract start date & end date)
        val txtContractDaysLeft = view.findViewById<TextView>(R.id.txt_contract_daysLeft)
        val txtContractStartDate = view.findViewById<TextView>(R.id.txt_start_date)
        val txtContractEndDate = view.findViewById<TextView>(R.id.txt_end_date)
        txtContractDaysLeft.text = "($daysLeft Days Left)"
        txtContractStartDate.text = contractStartDate
        txtContractEndDate.text = contractEndDate

        // Update days left text color
        if (daysLeft <= 7) txtContractDaysLeft.setTextColor(Color.parseColor("#FF0000"))
        else if (daysLeft <= 31)  txtContractDaysLeft.setTextColor(Color.parseColor("#F29D38"))
        else  txtContractDaysLeft.setTextColor(Color.parseColor("#009B06"))

        // Display user's owned coffee machines
        val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)
        createOwnedCoffeeMachines(mainLayout)
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
            )
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