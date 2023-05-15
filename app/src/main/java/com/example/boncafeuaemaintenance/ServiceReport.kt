package com.example.boncafeuaemaintenance

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServiceReport.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceReport : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_service_report, container, false)

        // References
        val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)

        // Display list of reports
        for (i in 0..5){
            val customLayoutReports = layoutInflater.inflate(R.layout.layout_bookings_reports, container, false)
            val reportImage = customLayoutReports.findViewById<ImageView>(R.id.img_insertImage)
            val reportTitle = customLayoutReports.findViewById<TextView>(R.id.txt_title)
            val reportCoffeeMachineName = customLayoutReports.findViewById<TextView>(R.id.txt_highlighter)

            // Set image
            reportImage.setImageResource(R.drawable.img_pdf)

            // Report title
            reportTitle.text = "Service Report"

            // Report's coffee machine name
            reportCoffeeMachineName.setTextColor(Color.parseColor("#5686E1"))
            reportCoffeeMachineName.text = "Coffee Machine name"

            mainLayout.addView(customLayoutReports)

            // Handle clicks for each reports
            customLayoutReports.setOnClickListener {
                // code
            }
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
         * @return A new instance of fragment ServiceReport.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ServiceReport().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}