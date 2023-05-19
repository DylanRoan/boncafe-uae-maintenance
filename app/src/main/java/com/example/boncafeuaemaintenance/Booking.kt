package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_booking.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Booking.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class Booking : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        // References
        val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)
        val buttonNewBooking = view.findViewById<Button>(R.id.btn_newBooking)

        // Display list of bookings
        /*for (i in 0..5){
            val customLayoutBooking = layoutInflater.inflate(R.layout.layout_bookings_reports, container, false)
            mainLayout.addView(customLayoutBooking)

            // Handle clicks for each booking
            customLayoutBooking.setOnClickListener {
                // code
            }
        }*/

        // 'Add New Booking' button
        buttonNewBooking.setOnClickListener{
            val intent = Intent(view.context, BookingCreate::class.java)
            startActivity(intent)
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
         * @return A new instance of fragment Booking.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Booking().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}