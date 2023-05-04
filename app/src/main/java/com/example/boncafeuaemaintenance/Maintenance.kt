package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.fragment_maintenance.*
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

        // Last maintenance date and calculate days ago
        val lastMaintenanceDate = "2023-03-11"
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

        // Inflate the layout for this fragment
        return view
    }

    /**
     * Calculate number of days ago since last maintenance date
     * @return number of days ago as int
     * */
    @SuppressLint("NewApi")
    fun getDaysAgo(date: String): Int {
        // Calculate days ago
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val pastDate = LocalDate.parse(date, formatter)
        val currentDate = LocalDate.now()
        val daysAgo = currentDate.toEpochDay() - pastDate.toEpochDay()

        // Result
        return daysAgo.toInt()
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