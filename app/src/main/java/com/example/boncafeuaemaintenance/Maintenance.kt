package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_maintenance.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import pl.droidsonroids.gif.GifImageView
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

        //get preference
        val prefName = "com.boncafe_maintenance.app"
        val prefs = view.context.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)

        val email = prefs.getString("$prefName.email", "none") as String
        val password = prefs.getString("$prefName.password", "none") as String

        //contract network call
        NetworkFunctions().maintenancePageRequest(view, view.context,
            "https://boncafe-backend.herokuapp.com/contract",
            ::setContract, password, email
        )
        //product network call
        NetworkFunctions().maintenancePageRequest(view, view.context,
            "https://boncafe-backend.herokuapp.com/table",
            ::setProducts, password, email
        )

        // Renew contract button
        val btnRenewContract = view.findViewById<Button>(R.id.btn_renewContract)
        btnRenewContract.setOnClickListener {
            popUpWindow(view.context)
        }

        return view
    }

    fun setContract(view : View, context : Context, jsonArray: JSONArray)
    {
        Log.i("BACKEND : CONTRACT", jsonArray.toString()) // DEBUG

        if (jsonArray.length() <= 0) return

        if (!jsonArray.getJSONObject(0).has("login")) //if login did not fail
        {

            // View references
            val contractPeriodLayout = view.findViewById<CardView>(R.id.AMC_period_container)
            val noProductsContractLayout = view.findViewById<LinearLayout>(R.id.layout_no_products_contract)
            val lastMaintenanceLayout = view.findViewById<CardView>(R.id.lastMaintenance_container)

            val contract = jsonArray.getJSONObject(0)

            activity?.runOnUiThread {
                // Update maintenance due date
                SetDate().updateMaintenanceDueDate(view, contract.getString("maintenance"))

                // Update contract ending date
                SetDate().updateContractDaysLeft(
                    view,
                    contract.getString("start_date"),
                    contract.getString("end_date")
                )

                // Set maintenance and contract layout visibility
                contractPeriodLayout.visibility = View.VISIBLE
                lastMaintenanceLayout.visibility = View.VISIBLE
                noProductsContractLayout.visibility = View.GONE

            }
        }
        else
        {
            //login failed
            //TODO GO BACK TO LOGIN PAGE TEST
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    fun setProducts(view : View, context : Context, jsonArray: JSONArray)
    {
        Log.i("BACKEND : PRODUCTS", jsonArray.toString()) // DEBUG

        val noProductsContractLayout = view.findViewById<LinearLayout>(R.id.layout_no_products_contract)

        if (jsonArray.length() <= 0) return

        if (!jsonArray.getJSONObject(0).has("login")) //if login did not fail
        {
            // Display user's owned coffee machines
            activity?.runOnUiThread {
                val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)
                createOwnedCoffeeMachines(mainLayout, jsonArray)
                noProductsContractLayout.visibility = View.GONE
            }
        }
        else
        {
            //login failed
            //TODO GO BACK TO LOGIN PAGE TEST
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
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
        popupText.text = "Do you want to renew your contract?"

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

            // TODO Send email for contract renewal

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
        val popupGif = popupBinding.findViewById<GifImageView>(R.id.gif_window)

        // Set Header and description
        popupHeader.text = "SUCCESS"
        popupText.text = "Your contract renewal has been sent to our email."
        btnYes.text = "OK"

        popupGif.visibility = View.VISIBLE
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
            myPopup.dismiss()
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