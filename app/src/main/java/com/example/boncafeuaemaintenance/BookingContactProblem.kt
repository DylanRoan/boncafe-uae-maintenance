package com.example.boncafeuaemaintenance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingContactProblem.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingContactProblem : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //declare view
        val view = inflater.inflate(R.layout.fragment_booking_contact_problem, container, false)

        val phoneEditText = view.findViewById<EditText>(R.id.input_phoneNum)
        val locationEditText = view.findViewById<EditText>(R.id.input_location)
        val detailsEditText = view.findViewById<EditText>(R.id.input_issue)

        sharedViewModel.setPhoneNumber(phoneEditText.text.toString())
        phoneEditText.setOnFocusChangeListener { v, hasFocus ->
            sharedViewModel.setPhoneNumber(phoneEditText.text.toString())
        }

        sharedViewModel.setLocation(locationEditText.text.toString())
        locationEditText.setOnFocusChangeListener { v, hasFocus ->
            sharedViewModel.setLocation(locationEditText.text.toString())
        }

        sharedViewModel.setDetails(detailsEditText.text.toString())
        detailsEditText.setOnFocusChangeListener { v, hasFocus ->
            sharedViewModel.setDetails(detailsEditText.text.toString())
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingContactProblem.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingContactProblem().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}