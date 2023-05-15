package com.example.boncafeuaemaintenance

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.activity_booking_create.*
import kotlinx.android.synthetic.main.layout_booking_coffeemachines.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingCoffeeMachines.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingCoffeeMachines : Fragment() {
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

    val storeInputSerialIDs = HashSet<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking_coffee_machines, container, false)

        // View References
        val linearLayoutCoffeeMachines = view.findViewById<LinearLayout>(R.id.linearLayout_coffeeMachines)
        val btnAddSerial = view.findViewById<ImageView>(R.id.btn_add_serial)
        val btnRemoveSerial = view.findViewById<ImageView>(R.id.btn_remove_serial)

        // Store first edit text to ViewModel
        val editTextSerial = view.findViewById<EditText>(R.id.input_serial)
        editTextSerial.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // NA
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Save the updated text to the SharedViewModel
                sharedViewModel.setData(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                // NA
            }
        })

        // Initialisations
        var serialInputTextCounter = 1
        val maxSerialInputs = 5

        // For setting button visibility
        fun checkButtonVisibility(){
            if (serialInputTextCounter<=1) {
                btnAddSerial.visibility = View.VISIBLE
                btnRemoveSerial.visibility = View.INVISIBLE
            } else if (serialInputTextCounter>=maxSerialInputs){
                btnAddSerial.visibility = View.INVISIBLE
                btnRemoveSerial.visibility = View.VISIBLE
            } else {
                btnAddSerial.visibility = View.VISIBLE
                btnRemoveSerial.visibility = View.VISIBLE
            }
        }

        // Button for adding EditText serial numbers
        btnAddSerial.setOnClickListener {
            if (serialInputTextCounter < maxSerialInputs){
                serialInputTextCounter++
                checkButtonVisibility()

                // References for EditText's Serial Number
                val inputSerial = EditText(view.context)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
                val newInputSerialID = View.generateViewId()

                // Adding properties to EditText's Serial Number
                inputSerial.id = newInputSerialID // create new id
                inputSerial.hint = "Serial No $serialInputTextCounter" //set hint
                inputSerial.setBackgroundResource(R.drawable.custom_textbox) // set background
                layoutParams.setMargins(0,10,0,10) // set margins
                inputSerial.layoutParams = layoutParams
                inputSerial.isSingleLine = true // set single line only

                // Add EditText's Serial Number to Parent Layout
                linearLayoutCoffeeMachines.addView(inputSerial)

                // Store new EditText's Serial Number ID to HashSet
                storeInputSerialIDs.add(newInputSerialID)

                // Store inputted Serial Number text to ViewModel
                inputSerial.setOnFocusChangeListener { v, hasFocus ->
                    // Check if user finishes using EditText and check if its not empty or doesn't include any whitespaces
                    if (!hasFocus && inputSerial.text.trim().toString().isNotEmpty()) {
                        // Get EditText's text
                        val enteredText = inputSerial.text.toString()

                        // Pass the inputted text to ViewModel
                        sharedViewModel.addSerialID(enteredText)
                    }
                }
            }
        }

        // Button for removing last EditText of Serial Number
        btnRemoveSerial.setOnClickListener {
            if (serialInputTextCounter > 1){
                serialInputTextCounter--
                checkButtonVisibility()

                // Init last EditText's Serial Number ID
                val lastInputSerialID = storeInputSerialIDs.last()

                // Find last EditText's Serial Number and remove it from parent layout
                val lastInputSerialView = view.findViewById<EditText>(lastInputSerialID)
                linearLayoutCoffeeMachines.removeView(lastInputSerialView)

                // Remove last EditText's Serial Number ID
                storeInputSerialIDs.remove(lastInputSerialID)
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
         * @return A new instance of fragment BookingCoffeeMachines.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingCoffeeMachines().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}