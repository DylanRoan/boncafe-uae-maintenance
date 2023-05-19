package com.example.boncafeuaemaintenance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONObject

class SharedViewModel : ViewModel() {

    //------ STORE SERIAL IDS ------//
    var serialIDs = JSONObject("{'Serial': ''}")
    var concatenatedSerialIDs : MutableLiveData<String> = MutableLiveData()
    var coffeMachineCount  : MutableLiveData<String> = MutableLiveData()

    // Store serial IDs
    fun setSerialID(name : String, inputSerialText: String) {
        serialIDs.put(name, inputSerialText)
        updateConcatenatedSerialIDs()

        Log.i("BACKEND", serialIDs.toString())
    }

    fun removeSerialID(name : String){
        serialIDs.remove(name)
        updateConcatenatedSerialIDs()
    }

    //--------------- COFFEE MACHINES ---------------//
    // Get all serial IDs and then put them into one string
    private fun updateConcatenatedSerialIDs() {

        var out = ""
        var count = 0
        Log.i("BACKEND", "Booking ${serialIDs.toString()}")
        if ((serialIDs.names() as JSONArray).length() > 1)
        {
            for (i in serialIDs.keys())
            {
                if (serialIDs.get(i) == "") {
                    out += "*None*\n"
                    count += 1
                }
                else {
                    out += "${serialIDs.get(i)}\n"
                }
            }

            count -= 1
            out = out.removeSuffix("*None*\n").removePrefix("\n")
        }
        else out = "*None*"

        Log.i("BACKEND", "Concatenated : $out")

        coffeMachineCount.value = count.toString()
        concatenatedSerialIDs.value = out
    }

    //--------------- DETAILS ---------------//
    var phoneNumberData : MutableLiveData<String> = MutableLiveData()
    var locationData : MutableLiveData<String> = MutableLiveData()
    var detailsData : MutableLiveData<String> = MutableLiveData()
    fun setPhoneNumber(text : String) { phoneNumberData.value = text.ifEmpty { "*None*" } }
    fun setLocation(text : String) { locationData.value = text.ifEmpty { "*None*" } }
    fun setDetails(text : String) { detailsData.value = text.ifEmpty { "*None*" } }
}