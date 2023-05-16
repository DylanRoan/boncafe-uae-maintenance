package com.example.boncafeuaemaintenance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class SharedViewModel : ViewModel() {

    //------ STORE SERIAL IDS ------//
    var serialIDs = JSONObject()
    var concatenatedSerialIDs: MutableLiveData<String> = MutableLiveData()

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

    // Get all serial IDs and then put them into one string
    private fun updateConcatenatedSerialIDs() {

        var out = ""
        for (i in serialIDs.keys())
        {
            out += "${serialIDs.get(i)}\n"
        }
        //remove the last \n at the end
        out = out.removeRange(out.length - 2, out.length - 1)
        Log.i("BACKEND", "Concatenated : $out")

        val concatenatedText = out
        concatenatedSerialIDs.value = concatenatedText
    }
    //------------------------------//

    private val getText = MutableLiveData<String>()

    fun setData(text: String) {
        getText.value = text
    }

    fun getData(): LiveData<String> {
        return getText
    }
}